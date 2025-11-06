package com.AssessAI.AssessAI.controllers;

import com.AssessAI.AssessAI.models.AppRole;
import com.AssessAI.AssessAI.models.Role;
import com.AssessAI.AssessAI.models.User;
import com.AssessAI.AssessAI.payloads.UserDTO;
import com.AssessAI.AssessAI.repository.RoleRepository;
import com.AssessAI.AssessAI.repository.UserRepository;
import com.AssessAI.AssessAI.security.jwt.JwtUtils;
import com.AssessAI.AssessAI.security.request.LoginRequest;
import com.AssessAI.AssessAI.security.request.SignupRequest;
import com.AssessAI.AssessAI.security.response.MessageResponse;
import com.AssessAI.AssessAI.security.response.UserInfoResponse;
import com.AssessAI.AssessAI.security.services.UserDetailsImpl;
import com.AssessAI.AssessAI.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ModelMapper modelMapper;

    // ---------------- Sign In ----------------
    @PostMapping("/signin")
    public ResponseEntity<?> signInUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Generate JWT token (not just cookie)
            String jwt = jwtUtils.getTokenFromUsername(userDetails.getUsername());

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .toList();

            // Include token in response
            UserInfoResponse userInfoResponse = new UserInfoResponse(
                    userDetails.getId(),
                    userDetails.getUsername(),
                    roles,
                    jwt
            );

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(userInfoResponse);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("ERROR: Invalid username or password"));
        }
    }


    // ---------------- Sign Up ----------------
    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@Valid @RequestBody SignupRequest signupRequest) {
        // Check if username/email already exists
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("ERROR: Username already exists"));
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("ERROR: Email already exists"));
        }

        // Determine role
        final AppRole selectedRole;
        if (signupRequest.getRole() != null) {
            try {
                selectedRole = AppRole.valueOf(signupRequest.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("ERROR: Invalid role"));
            }
        } else {
            selectedRole = AppRole.ROLE_STUDENT; // Default
        }

        // Fetch Role entity from DB or create if not exists
        Role role = roleRepository.findByRoleName(selectedRole)
                .orElseGet(() -> roleRepository.save(new Role(selectedRole)));

        // Create new User
        User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()),
                selectedRole
        );

        userService.saveUser(modelMapper.map(user, UserDTO.class));

        return ResponseEntity.ok(new MessageResponse("User Registered Successfully!"));
    }

    // ---------------- Get Current User Info ----------------
    @GetMapping("/me")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("User not authenticated"));
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        UserInfoResponse userInfoResponse = new UserInfoResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                roles
        );

        return ResponseEntity.ok(userInfoResponse);
    }

    // ---------------- Sign Out ----------------
    @PostMapping("/signout")
    public ResponseEntity<?> signoutUser() {
        ResponseCookie cookie = jwtUtils.clearJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You have been signed out!"));
    }

    // ---------------- CRUD Operations ----------------
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
