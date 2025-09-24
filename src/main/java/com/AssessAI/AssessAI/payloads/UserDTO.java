package com.AssessAI.AssessAI.payloads;

import com.AssessAI.AssessAI.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;          // same as entity's id
    private String username;
    private String email;
    private String password;
    private Role role;

}
