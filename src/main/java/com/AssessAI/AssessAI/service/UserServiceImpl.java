package com.AssessAI.AssessAI.service;

import com.AssessAI.AssessAI.models.Role;
import com.AssessAI.AssessAI.models.Student;
import com.AssessAI.AssessAI.models.Teacher;
import com.AssessAI.AssessAI.models.User;
import com.AssessAI.AssessAI.payloads.UserDTO;
import com.AssessAI.AssessAI.repository.UserRepository;
import com.AssessAI.AssessAI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(UserDTO user) {
        User savedUser= new User(user.getUsername(),user.getEmail(),user.getPassword(),user.getRole());
        if(user.getRole().equals(Role.ROLE_STUDENT)) {
            Student student = new Student();
            student.setFullName(user.getFullName());
            savedUser.setStudent(student);
        }
        else {
            Teacher teacher = new Teacher();
            teacher.setFullName(user.getFullName());
            savedUser.setTeacher(teacher);
        }
        return userRepository.save(savedUser);
    }

    @Override
    public User updateUser(Long id, User user) {
        Optional<User> existing = userRepository.findById(id);
        if(existing.isPresent()) {
            User u = existing.get();
            u.setUsername(user.getUsername());
            u.setEmail(user.getEmail());
            u.setPassword(user.getPassword());
            u.setRole(user.getRole());
            return userRepository.save(u);
        }
        return null; // ya exception throw karo
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
