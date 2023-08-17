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
    public UUID createQuestion(String username, CreateQuestionDto questionDto) {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setQuestion(questionDto.question());
        questionEntity.setOpenToPublic(questionDto.openToPublic());
        questionEntity.setStarter(username);
        QuestionEntity.persist(questionEntity);
        return questionEntity.id;
    }

    public List<QuestionEntity> getQuestions(final String username, final String question, Boolean answered) {
        Map<String, Object> paramMap = new HashMap<>();

        String query = "SELECT q " +
                "FROM QuestionEntity q " +
                "LEFT JOIN q.responders r " +
                "WHERE (q.starter = :username OR r.username = :username)";

        paramMap.put("username", username);

        if (question != null) {
            query += " AND question like :question";
            paramMap.put("question", "%" + question + "%");
        }
        if (answered != null) {
            query += " AND answered = :answered";
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
    public void updateQuestion(UUID questionId, String username, QuestionDto questionDto) {
        QuestionEntity.update("question = ?3, answered = ?4, openToPublic = ?5 WHERE  id = ?1 AND starter = ?2", questionId, username, questionDto.question(), questionDto.answered(), questionDto.openToPublic());
    }

    public boolean isQuestionPublic(String questionId) {
        return QuestionEntity.count("id = ?1 and openToPublic = true", UUID.fromString(questionId)) == 1;
    }
}
