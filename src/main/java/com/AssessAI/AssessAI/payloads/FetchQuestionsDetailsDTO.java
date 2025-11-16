package com.AssessAI.AssessAI.payloads;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class FetchQuestionsDetailsDTO {
    private List<QuestionDTO> questions;
}
