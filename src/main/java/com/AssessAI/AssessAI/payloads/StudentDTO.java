package com.AssessAI.AssessAI.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {

    private Long id;              // corresponds to entity's stId
    private String fullName;
    private String enrollmentNo;
    private String contactNo;
    private String address;

    private UserDTO user;

}
