package com.mhtcet.evaluation_service.client;

import com.mhtcet.evaluation_service.dto.AnswerKeyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

// BEFORE: @FeignClient(name = "question-bank-service", url = "${qbank.service.url}")
@FeignClient(name = "question-bank-service")
public interface QuestionBankClient {

    @PostMapping("/api/questions/answer-keys")
    List<AnswerKeyDTO> getAnswerKeys(@RequestBody List<Long> questionIds);
}
