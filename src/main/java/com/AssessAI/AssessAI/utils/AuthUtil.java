package com.AssessAI.AssessAI.utils;

import com.AssessAI.AssessAI.models.User;
import com.AssessAI.AssessAI.models.Teacher;
import com.AssessAI.AssessAI.models.Student;
import com.AssessAI.AssessAI.models.AppRole;
import com.AssessAI.AssessAI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private UserRepository userRepository;

    // Logged in user details fetch karna
    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found.."));
    }

    public String loggedInEmail() {
        return loggedInUser().getEmail();
    }

    public Long loggedInUserId() {
        return loggedInUser().getId();
    }

    public boolean isTeacher() {
        return loggedInUser().getAppRole() == AppRole.ROLE_TEACHER;
    }

    public boolean isStudent() {
        return loggedInUser().getAppRole() == AppRole.ROLE_STUDENT;
    }

    public Teacher getLoggedInTeacher() {
        if (!isTeacher()) {
            throw new RuntimeException("Logged in user is not a Teacher");
        }
        return loggedInUser().getTeacher();
    }

    public Student getLoggedInStudent() {
        if (!isStudent()) {
            throw new RuntimeException("Logged in user is not a Student");
        }
        return loggedInUser().getStudent();
    }
}
