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

            Hãy tạo 5 câu hỏi phỏng vấn kỹ thuật phù hợp với vị trí trên.

            YÊU CẦU ĐỊNH DẠNG TRẢ VỀ:
            - CHỈ trả về JSON nguyên bản (không có text giải thích, không có dấu ```).
            - Cấu trúc JSON là một mảng các đối tượng có các field:
              "content": nội dung câu hỏi,
              "expectedAnswer": câu trả lời đầy đủ và chi tiết mà một ứng viên giỏi cần trả lời,
              "keywords": mảng các từ khóa kỹ thuật cốt lõi liên quan đến câu hỏi,
              "difficulty": một trong các giá trị easy, medium, hard,
              "level": một trong các giá trị junior, mid, senior,
              "rubrics": mảng gồm đúng 5 đối tượng với scoreLevel từ 1 đến 5.

            YÊU CẦU CHI TIẾT VỀ RUBRICS — mỗi câu hỏi BẮT BUỘC có đủ 5 rubric:
            - scoreLevel 1: Ứng viên không trả lời được hoặc trả lời sai hoàn toàn. Mô tả cụ thể biểu hiện của mức này.
            - scoreLevel 2: Ứng viên có hiểu biết mơ hồ, nhắc đến đúng khái niệm nhưng giải thích thiếu chính xác hoặc không đầy đủ. Mô tả cụ thể biểu hiện của mức này.
            - scoreLevel 3: Ứng viên trả lời đúng các ý chính, hiểu được khái niệm cơ bản nhưng chưa đề cập đến các trường hợp ngoại lệ hoặc best practice. Mô tả cụ thể biểu hiện của mức này.
            - scoreLevel 4: Ứng viên trả lời đầy đủ, có ví dụ minh họa thực tế, đề cập được ít nhất một best practice hoặc trade-off. Mô tả cụ thể biểu hiện của mức này.
            - scoreLevel 5: Ứng viên trả lời xuất sắc, giải thích sâu, đề cập đến edge case, so sánh các giải pháp, có kinh nghiệm thực tế chứng minh. Mô tả cụ thể biểu hiện của mức này.

            Mỗi criteriaDescription phải mô tả RÕ RÀNG và CỤ THỂ ứng viên cần nói gì, biết gì để đạt mức điểm đó — không được viết chung chung.
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
