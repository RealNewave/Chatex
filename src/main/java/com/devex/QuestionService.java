package com.devex;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class QuestionService {

    @Transactional
    public UUID createQuestion(SubjectDto subjectDto) {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setQuestion(subjectDto.subject());
        questionEntity.setStarter(subjectDto.username());
        QuestionEntity.persist(questionEntity);
        return questionEntity.id;
    }

    public List<QuestionEntity> getQuestions(String username) {
        List<QuestionEntity> questionEntities = QuestionEntity.getEntityManager()
                .createQuery(
                        "SELECT q " +
                        "FROM QuestionEntity q " +
                        "LEFT JOIN q.answers a " +
                        "WHERE q.starter = :username OR a.username = :username")
                .setParameter("username", username)
                .getResultList();

        return questionEntities;
    }

    @Transactional
    public AnswerEntity answerQuestion(UUID questionId, AnswerDto answerDto) {
        Optional<QuestionEntity> questionEntityOptional = QuestionEntity.findByIdOptional(questionId);
        if(questionEntityOptional.isPresent()){
            QuestionEntity questionEntity = questionEntityOptional.get();
            AnswerEntity answerEntity = new AnswerEntity();
            answerEntity.setUsername(answerDto.username());
            answerEntity.setAnswer(answerDto.answer());
            answerEntity.persistAndFlush();

            ResponderEntity responderEntity = new ResponderEntity();
            responderEntity.setUsername(answerDto.username());
            responderEntity.persist();

            questionEntity.getAnswers().add(answerEntity);
            questionEntity.persist();
            return answerEntity;

        } else {
            throw new NotFoundException();
        }
    }

    public QuestionEntity getQuestion(UUID questionId) {
        return QuestionEntity.findById(questionId);
    }
}
