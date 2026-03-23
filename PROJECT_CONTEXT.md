# Project Overview: AI Interview Copilot
A B2B SaaS platform for IT Technical Interviews to eliminate interview bias and automate post-interview scoring using AI.

## Tech Stack
- **Backend:** Java 17+, Spring Boot 3.x (Microservices Architecture).
- **Frontend:** React 18+ (Vite), Tanstack Router, Tailwind CSS, Zustand (State Management), Axios.
- **Database:** PostgreSQL.
- **Messaging:** RabbitMQ.
- **AI/LLM:** OpenAI API (for mock) / Gemini API.
- **Build Tool:** Maven.

## Architecture Guidelines
1. **Microservices:**
   - `api-gateway`: Spring Cloud Gateway (Routing & CORS).
   - `interview-cms-service`: Manages static data (Users, Questions, Rubrics, Skill Tags). RESTful.
   - `interview-session-service`: Manages active interviews, real-time notes. Uses Spring Web/WebSocket.
   - `ai-worker-service`: Asynchronous worker. Listens to RabbitMQ, processes audio, calls LLM, saves evaluations.
2. **Coding Standards:**
   - Use DTOs for request/response. Never expose Entity directly.
   - Use MapStruct for Entity <-> DTO mapping.
   - Global Exception Handling (ControllerAdvice) with standard error responses.
   - Flyway or Liquibase for database migrations.

## Database Schema (ERD)
[AI IDE: Refer to the logical relationships below for Entity creation]
- USER (id, full_name, email, role)
- CANDIDATE (id, full_name, email, cv_url, applied_position)
- SKILL_TAG (id, name, category)
- QUESTION (id, content, expected_answer, keywords (JSONB), level)
- RUBRIC (id, question_id, score_level, criteria_description)
- INTERVIEW_SESSION (id, interviewer_id, candidate_id, status, started_at, ended_at, ai_overall_summary, final_ai_score)
- SESSION_RECORDING (id, session_id, audio_storage_url, full_transcript)
- INTERVIEWER_NOTE (id, session_id, question_id, tag_type, note_content, timestamp_seconds)
- QUESTION_EVALUATION (id, session_id, question_id, ai_score, interviewer_score, ai_evidence, ai_feedback)