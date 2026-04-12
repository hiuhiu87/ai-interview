package com.aiinterview.cms.controller;

import com.aiinterview.cms.dto.admin.GenerateFromJDRequest;
import com.aiinterview.cms.dto.admin.GenerateQuestionSetRequest;
import com.aiinterview.cms.dto.admin.QuestionRequest;
import com.aiinterview.cms.dto.admin.QuestionResponse;
import com.aiinterview.cms.dto.admin.QuestionSetResponse;
import com.aiinterview.cms.service.QuestionBuilderService;
import com.aiinterview.common.constant.RequestMappingConstant;
import com.aiinterview.common.response.CommonResponse;
import com.aiinterview.common.response.CommonResponseFactory;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(RequestMappingConstant.API_VERSION_PREFIX + "/cms")
public class CmsQuestionController {

    private final QuestionBuilderService questionBuilderService;

    public CmsQuestionController(QuestionBuilderService questionBuilderService) {
        this.questionBuilderService = questionBuilderService;
    }

    @PostMapping("/questions")
    public ResponseEntity<CommonResponse<QuestionResponse>> createQuestion(@Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponseFactory.success(questionBuilderService.createQuestion(request), "Created"));
    }

    @GetMapping("/questions")
    public ResponseEntity<CommonResponse<List<QuestionResponse>>> getQuestions() {
        return ResponseEntity.ok(CommonResponseFactory.success(questionBuilderService.getQuestions()));
    }

    @PostMapping("/question-set/generate")
    public ResponseEntity<CommonResponse<QuestionSetResponse>> generateQuestionSet(
            @Valid @RequestBody GenerateQuestionSetRequest request
    ) {
        return ResponseEntity.ok(CommonResponseFactory.success(questionBuilderService.generateQuestionSet(request)));
    }

    @PostMapping("/questions/generate-from-jd")
    public ResponseEntity<CommonResponse<List<QuestionResponse>>> generateFromJD(
            @Valid @RequestBody GenerateFromJDRequest request
    ) {
        List<QuestionResponse> questions = questionBuilderService.generateQuestionsFromJD(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponseFactory.success(questions, "Generated " + questions.size() + " questions"));
    }

}
