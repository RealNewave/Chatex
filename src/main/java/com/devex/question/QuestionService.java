package com.devex.question;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.*;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    @Transactional
    public UUID createQuestion(String username, String subject) {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setQuestion(subject);
        questionEntity.setStarter(username);
        QuestionEntity.persist(questionEntity);
        return questionEntity.id;
    }

    public List<QuestionEntity> getQuestions(final String username, final String question, Boolean answered) {

        Map<String, Object> paramMap = new HashMap<>();

        String query = "SELECT q " +
                "FROM QuestionEntity q " +
                "LEFT JOIN q.answers a " +
                "WHERE (q.starter = :username OR a.username = :username)";

        paramMap.put("username", username);

        if (question != null) {
            query += " AND q.question like :question";
            paramMap.put("question", question);
        }
        if (answered != null) {
            query += " AND q.answered = :answered";
            paramMap.put("answered", answered);
        }
        return QuestionEntity.list(query, paramMap);
    }

    @Transactional
    public AnswerEntity answerQuestion(final UUID questionId, final String username, final String answer) {
        Optional<QuestionEntity> questionEntityOptional = QuestionEntity.findByIdOptional(questionId);
        if (questionEntityOptional.isPresent()) {
            QuestionEntity questionEntity = questionEntityOptional.get();
            if (questionEntity.isAnswered()) {
                throw new BadRequestException("Question already closed");
            }
            AnswerEntity answerEntity = new AnswerEntity();
            answerEntity.setUsername(username);
            answerEntity.setAnswer(answer);
            answerEntity.persistAndFlush();

            questionEntity.getAnswers().add(answerEntity);
            questionEntity.setUpdated(ZonedDateTime.now());
            questionEntity.persist();
            return answerEntity;

        } else {
            throw new NotFoundException();
        }
    }

    public QuestionEntity getQuestion(final UUID questionId) {
        return QuestionEntity.findById(questionId);
    }

    @Transactional
    public void closeQuestion(UUID questionId, String username) {
        QuestionEntity.update("answered = true WHERE starter = ?1 AND id = ?2", username, questionId);
    }
}
