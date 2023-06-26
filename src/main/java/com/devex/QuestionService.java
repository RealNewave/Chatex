package com.devex;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class QuestionService {

    @Transactional
    public long createQuestion(SubjectDto subjectDto) {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setQuestion(subjectDto.subject());
        questionEntity.setStarter(subjectDto.username());
        QuestionEntity.persist(questionEntity);
        return questionEntity.id;
    }

    public List<QuestionEntity> getQuestions(String username) {
        List<QuestionEntity> questionEntities = QuestionEntity.getEntityManager()
                .createQuery("SELECT q FROM QuestionEntity q WHERE starter =  :username OR :username in (SELECT username from AnswerEntity)")
                .setParameter("username", username)
                .getResultList();

        return questionEntities;


    }

    @Transactional
    public void answerQuestion(Long questionId, AnswerDto answerDto) {
        Optional<QuestionEntity> questionEntityOptional = QuestionEntity.findByIdOptional(questionId);
        questionEntityOptional.ifPresentOrElse(questionEntity -> {
            AnswerEntity answerEntity = new AnswerEntity();
            answerEntity.setUsername(answerDto.username());
            answerEntity.setAnswer(answerDto.answer());
            answerEntity.persist();

            ResponderEntity responderEntity = new ResponderEntity();
            responderEntity.setUsername(answerDto.username());
            responderEntity.persist();

            questionEntity.getAnswers().add(answerEntity);
            questionEntity.persist();
        }, NotFoundException::new);
    }

    public QuestionEntity getQuestion(Long questionId) {
        return QuestionEntity.findById(questionId);
    }
}
