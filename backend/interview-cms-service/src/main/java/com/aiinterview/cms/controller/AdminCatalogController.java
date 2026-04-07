package com.aiinterview.cms.controller;

import com.aiinterview.cms.dto.admin.MetadataResponse;
import com.aiinterview.cms.dto.admin.QuestionRequest;
import com.aiinterview.cms.dto.admin.QuestionResponse;
import com.aiinterview.cms.dto.admin.SkillRequest;
import com.aiinterview.cms.dto.admin.SkillResponse;
import com.aiinterview.cms.dto.admin.TagRequest;
import com.aiinterview.cms.dto.admin.TagResponse;
import com.aiinterview.cms.dto.admin.TemplateRequest;
import com.aiinterview.cms.dto.admin.TemplateResponse;
import com.aiinterview.cms.service.AdminCatalogService;
import com.aiinterview.common.constant.RequestMappingConstant;
import com.aiinterview.common.response.CommonResponse;
import com.aiinterview.common.response.CommonResponseFactory;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(RequestMappingConstant.API_VERSION_PREFIX + "/admin/catalog")
public class AdminCatalogController {

    private final AdminCatalogService adminCatalogService;

    public AdminCatalogController(AdminCatalogService adminCatalogService) {
        this.adminCatalogService = adminCatalogService;
    }

    @GetMapping("/metadata")
    public ResponseEntity<CommonResponse<MetadataResponse>> metadata() {
        return ResponseEntity.ok(CommonResponseFactory.success(adminCatalogService.getMetadata()));
    }

    @GetMapping("/skills")
    public ResponseEntity<CommonResponse<List<SkillResponse>>> getSkills() {
        return ResponseEntity.ok(CommonResponseFactory.success(adminCatalogService.getSkills()));
    }

    @PostMapping("/skills")
    public ResponseEntity<CommonResponse<SkillResponse>> createSkill(@Valid @RequestBody SkillRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponseFactory.success(adminCatalogService.createSkill(request), "Created"));
    }

    @PutMapping("/skills/{id}")
    public ResponseEntity<CommonResponse<SkillResponse>> updateSkill(@PathVariable Integer id, @Valid @RequestBody SkillRequest request) {
        return ResponseEntity.ok(CommonResponseFactory.success(adminCatalogService.updateSkill(id, request), "Updated"));
    }

    @DeleteMapping("/skills/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteSkill(@PathVariable Integer id) {
        adminCatalogService.deleteSkill(id);
        return ResponseEntity.ok(CommonResponseFactory.success(null, "Deleted"));
    }

    @GetMapping("/tags")
    public ResponseEntity<CommonResponse<List<TagResponse>>> getTags() {
        return ResponseEntity.ok(CommonResponseFactory.success(adminCatalogService.getTags()));
    }

    @PostMapping("/tags")
    public ResponseEntity<CommonResponse<TagResponse>> createTag(@Valid @RequestBody TagRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponseFactory.success(adminCatalogService.createTag(request), "Created"));
    }

    @PutMapping("/tags/{id}")
    public ResponseEntity<CommonResponse<TagResponse>> updateTag(@PathVariable Integer id, @Valid @RequestBody TagRequest request) {
        return ResponseEntity.ok(CommonResponseFactory.success(adminCatalogService.updateTag(id, request), "Updated"));
    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteTag(@PathVariable Integer id) {
        adminCatalogService.deleteTag(id);
        return ResponseEntity.ok(CommonResponseFactory.success(null, "Deleted"));
    }

    @GetMapping("/templates")
    public ResponseEntity<CommonResponse<List<TemplateResponse>>> getTemplates() {
        return ResponseEntity.ok(CommonResponseFactory.success(adminCatalogService.getTemplates()));
    }

    @PostMapping("/templates")
    public ResponseEntity<CommonResponse<TemplateResponse>> createTemplate(@Valid @RequestBody TemplateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponseFactory.success(adminCatalogService.createTemplate(request), "Created"));
    }

    @PutMapping("/templates/{id}")
    public ResponseEntity<CommonResponse<TemplateResponse>> updateTemplate(@PathVariable Integer id, @Valid @RequestBody TemplateRequest request) {
        return ResponseEntity.ok(CommonResponseFactory.success(adminCatalogService.updateTemplate(id, request), "Updated"));
    }

    @DeleteMapping("/templates/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteTemplate(@PathVariable Integer id) {
        adminCatalogService.deleteTemplate(id);
        return ResponseEntity.ok(CommonResponseFactory.success(null, "Deleted"));
    }

    @GetMapping("/questions")
    public ResponseEntity<CommonResponse<List<QuestionResponse>>> getQuestions() {
        return ResponseEntity.ok(CommonResponseFactory.success(adminCatalogService.getQuestions()));
    }

    @PostMapping("/questions")
    public ResponseEntity<CommonResponse<QuestionResponse>> createQuestion(@Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponseFactory.success(adminCatalogService.createQuestion(request), "Created"));
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<CommonResponse<QuestionResponse>> updateQuestion(@PathVariable UUID id, @Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.ok(CommonResponseFactory.success(adminCatalogService.updateQuestion(id, request), "Updated"));
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteQuestion(@PathVariable UUID id) {
        adminCatalogService.deleteQuestion(id);
        return ResponseEntity.ok(CommonResponseFactory.success(null, "Deleted"));
    }
}
