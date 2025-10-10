package com.AssessAI.AssessAI.security.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String username;
    private List<String> roles; // e.g., ["ROLE_STUDENT"]
    private String token;       // JWT token

    public UserInfoResponse(Long id, String username, List<String> roles) {
    }
}
