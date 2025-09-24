package com.AssessAI.AssessAI.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {

    private Long id;              // corresponds to entity's stId
    private String fullName;
    private String enrollmentNo;
    private String contactNo;
    private String address;

    private UserDTO user;

}
