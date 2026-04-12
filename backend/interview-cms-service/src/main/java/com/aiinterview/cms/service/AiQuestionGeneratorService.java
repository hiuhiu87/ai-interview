package com.aiinterview.cms.service;

import com.aiinterview.cms.dto.admin.AiQuestionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiQuestionGeneratorService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatClient chatClient;

    public AiQuestionGeneratorService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public List<AiQuestionDto> generateQuestionsFromJD(String jobDescription) {
        String prompt = """
            Dựa trên mô tả công việc (JD) sau:
            
            %s
            
            Hãy tạo 5 câu hỏi phỏng vấn kỹ thuật.
            YÊU CẦU ĐỊNH DẠNG TRẢ VỀ:
            - CHỈ trả về JSON nguyên bản (không có text giải thích, không có dấu ```).
            - Cấu trúc JSON là một mảng các đối tượng có các field:
              "content": nội dung câu hỏi,
              "expectedAnswer": câu trả lời kỳ vọng,
              "keywords": mảng các từ khóa string,
              "difficulty": một trong các giá trị easy, medium, hard,
              "level": một trong các giá trị junior, mid, senior,
              "rubrics": mảng gồm các đối tượng có scoreLevel (số nguyên) và criteriaDescription (string).
            - Mỗi câu hỏi phải có ít nhất 3 rubrics với scoreLevel là 1, 3, 5.
            """.formatted(jobDescription);

        String raw = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        try {
            String cleaned = raw.replaceAll("(?s)```json|```", "").trim();
            return objectMapper.readValue(
                    cleaned,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, AiQuestionDto.class)
            );
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("AI returned invalid JSON: " + e.getMessage(), e);
        }
    }
}
