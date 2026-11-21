package com.mhtcet.question_bank_service.dto;

import java.util.List;

public record MockPaperResponse(
   List<StudentQuestionDTO> section1,
   List<StudentQuestionDTO> section2
) {}
