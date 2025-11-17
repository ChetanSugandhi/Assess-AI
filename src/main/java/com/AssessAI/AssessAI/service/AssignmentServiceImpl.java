package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.*;
import com.AssessAI.AssessAI.payloads.*;
import com.AssessAI.AssessAI.repository.*;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ParagraphRepository paragraphRepository;

    @Autowired
    private MCQRepository mcqRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private StudentRepository studentRepository;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    // create (save) assignment
    @Override
    public Long saveAssignment(AssignmentDTO assignmentDTO) {

        if(!classroomRepository.existsByClassroomCode(assignmentDTO.getClassroomCode())) {
            throw new IllegalArgumentException("Classroom with classroom code : " + assignmentDTO.getClassroomCode() + " not found..!!");
        }

        Assignment newAssignemnt = new Assignment();
        newAssignemnt.setTitle(assignmentDTO.getTitle());
        newAssignemnt.setDescription(assignmentDTO.getDescription());

        Optional<Classroom> fetchClassroom = classroomRepository.findByClassroomCode(assignmentDTO.getClassroomCode());
        if(fetchClassroom.isPresent()) {
            Classroom fetchClassroomConvert = fetchClassroom.get();
            newAssignemnt.setClassroom(fetchClassroomConvert);
        }


        Assignment saveAssignment = assignmentRepository.save(newAssignemnt);

        if(saveAssignment.getAsgnId() != null) {
            return saveAssignment.getAsgnId();
        }

        return (long) -1;

//        return modelMapper.map(saveAssignment, AssignmentDTO.class);
    }

    @Override
    public FetchQuestionsDetailsDTO getAssignmentById(Long id) {

        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id : " + id));

        List<Question> questions = questionRepository.findQuestionsByAssignmentId(id);

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question q : questions) {

            QuestionDTO dto = new QuestionDTO();
            dto.setQId(q.getQId());
            dto.setText(q.getText());

            // MCQ Case
            if (q.getMcq() != null) {
                dto.setType("MCQ");

                MCQ mcq = q.getMcq();

                MCQDTO mcqDTO = MCQDTO.builder()
                        .option1(mcq.getOptionA())
                        .option2(mcq.getOptionB())
                        .option3(mcq.getOptionC())
                        .option4(mcq.getOptionD())
                        .build();

                dto.setMcq(mcqDTO);
            }

            // Paragraph Case
            else if (q.getPara() != null) {
                dto.setType("PARAGRAPH");
                dto.setMcq(null);
            }

            else {
                dto.setType("UNKNOWN");
            }

            questionDTOList.add(dto);
        }

        // FINAL RETURN DTO
        FetchQuestionsDetailsDTO response = new FetchQuestionsDetailsDTO();
        response.setQuestions(questionDTOList);

        return response;
    }




    @Override
    @Transactional
    public AssignmentDTO updateAssignment(Long id, AssignmentDTO assignmentDTO) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + id));

        // Update basic fields
        if (assignmentDTO.getTitle() != null)
            assignment.setTitle(assignmentDTO.getTitle());
        if (assignmentDTO.getDescription() != null)
            assignment.setDescription(assignmentDTO.getDescription());

        // Update classroom if provided

        Assignment updated = assignmentRepository.save(assignment);
        return modelMapper.map(updated, AssignmentDTO.class);
    }

    @Override
    @Transactional
    public String deleteAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));
        assignmentRepository.delete(assignment);
        return "Assignment deleted successfully";
    }

    @Override
    public List<AssignmentDTO> getAllAssignmentOfClassroom(String classroomCode) {

        if(!classroomRepository.existsByClassroomCode(classroomCode)) {
            throw new IllegalArgumentException("Classroom not found with classroom code : " + classroomCode);
        }

        Optional<Classroom> fetch = classroomRepository.findByClassroomCode(classroomCode);
        Classroom fetchClassroom = null;
        if(fetch.isPresent()) {
            fetchClassroom = fetch.get();
        }



        Set<Assignment> fetchAssignment = fetchClassroom.getAssignments();
        List<AssignmentDTO> assignmentDTOS = new ArrayList<>();

        for(Assignment eachAssignment : fetchAssignment) {
            AssignmentDTO assignmentDTO = modelMapper.map(eachAssignment, AssignmentDTO.class);
            assignmentDTOS.add(assignmentDTO);
        }

        return assignmentDTOS;
    }

    @Override
    public List<Assignment> getAttemptedAssignments(Long studentId, Long classroomId) {
        return responseRepository.findAttemptedAssignmentsInClassroom(studentId, classroomId);
    }


    @Override
    public String getAttemptedAssignmentsQuestionAnswers(Long studentId, Long classroomId) throws Exception {


        Student student = studentRepository.findById(studentId).orElseThrow();
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow();


        // 1️⃣ Check if student attempted ANY assignment
        List<Assignment> assignments =
                responseRepository.findAttemptedAssignmentsInClassroom(studentId, classroomId);

        if (assignments == null || assignments.isEmpty()) {
            return "You have not attempted any assignment in this classroom yet. "
                    + "Attempt at least one assignment to generate feedback.";
        }

        // 2️⃣ Fetch existing feedback (if any)
        Feedback existingFeedback =
                feedbackRepository.findByStudent_StIdAndClassroom_cId(studentId, classroomId);

        // Prepare question-answer list for AI
        List<QuestionAnswerDTO> qaList = new ArrayList<>();

        for (Assignment assignment : assignments) {

            // Fetch ONLY this student's responses for this assignment
            List<Response> responses =
                    responseRepository.findResponsesForAssignment(studentId, assignment.getAsgnId());

            for (Response r : responses) {

                Question q = r.getQuestion();
                QuestionAnswerDTO dto = new QuestionAnswerDTO();

                dto.setQuestion(q.getText());

                // 🔹 MCQ Conversion
                if (q.getMcq() != null) {

                    String ansKey = r.getAnswer();
                    MCQ mcq = q.getMcq();

                    String finalAnswer = switch (ansKey.toUpperCase()) {
                        case "A" -> mcq.getOptionA();
                        case "B" -> mcq.getOptionB();
                        case "C" -> mcq.getOptionC();
                        case "D" -> mcq.getOptionD();
                        default -> ansKey;
                    };

                    dto.setAnswer(finalAnswer);
                } else {
                    dto.setAnswer(r.getAnswer());
                }

                qaList.add(dto);
            }
        }

        // 3️⃣ Generate new feedback using AI
        String newFeedback = feedbackService.generateFeedback(qaList);

        // 4️⃣ Store or update feedback in database
        if (existingFeedback == null) {

            // First time feedback generation — SAVE NEW
            Feedback fb = new Feedback();
            fb.setComments(newFeedback);          // AI FEEDBACK
            fb.setFeedbackDate(LocalDateTime.now());
            fb.setTeacherAddonData(null);
            fb.setStudent(student);
            fb.setTeacher(null);
            fb.setClassroom(classroom);

            feedbackRepository.save(fb);

        } else {

            // Update existing feedback
            existingFeedback.setComments(newFeedback);
            existingFeedback.setFeedbackDate(LocalDateTime.now());

            feedbackRepository.save(existingFeedback);
        }


        return newFeedback;
    }


    @Override
    public Long fetchTestId(Long assignmentId) {
        return testRepository.findTestIdByAssignmentId(assignmentId);
    }

}
