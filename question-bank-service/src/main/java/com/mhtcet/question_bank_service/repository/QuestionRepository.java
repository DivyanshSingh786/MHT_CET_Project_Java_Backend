package com.mhtcet.question_bank_service.repository;

import com.mhtcet.question_bank_service.dto.StudentQuestionDTO;
import com.mhtcet.question_bank_service.model.Question;
import com.mhtcet.question_bank_service.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "SELECT * FROM questions WHERE subject = :subject AND standard = :standard ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Question> findRandomQuestions(@Param("subject") String subject,
                                       @Param("standard") String standard,
                                       @Param("limit") int limit);

    List<Question> findBySubject(Subject subject);
}
