package com.devex.question;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public List<QuestionEntity> getQuestions(final String username, final String question) {

        String query = "SELECT q " +
                "FROM QuestionEntity q " +
                "LEFT JOIN q.answers a " +
                "WHERE (q.starter = ?1 OR a.username = ?1)";

        if (question != null) {
            query += " AND q.question like ?2";
            return QuestionEntity.list(query, username, "%" + question + "%");
        }
        return QuestionEntity.list(query, username);


    }

    @Transactional
    public AnswerEntity answerQuestion(final UUID questionId, final String username, final String answer) {
        Optional<QuestionEntity> questionEntityOptional = QuestionEntity.findByIdOptional(questionId);
        if (questionEntityOptional.isPresent()) {
            QuestionEntity questionEntity = questionEntityOptional.get();
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
}
