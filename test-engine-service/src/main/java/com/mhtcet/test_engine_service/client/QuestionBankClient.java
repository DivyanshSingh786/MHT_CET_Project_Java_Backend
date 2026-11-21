package com.mhtcet.test_engine_service.client;

import com.mhtcet.test_engine_service.dto.MockPaperResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// BEFORE: @FeignClient(name = "question-bank-service", url = "${qbank.service.url}")
@FeignClient(name = "question-bank-service")
public interface QuestionBankClient {

    @GetMapping("/api/questions/mock-paper")
    MockPaperResponse getMockPaper(@RequestParam("stream") String stream);
}
