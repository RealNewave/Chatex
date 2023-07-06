package com.devex;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/api/v1/questions/{questionId}/{username}",
encoders = QuestionEncoder.class)
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class QuestionSocket {

    final Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    final QuestionService questionService;
    final ManagedExecutor managedExecutor;

    @OnOpen
    public void onOpen(Session session, @PathParam("questionId") String questionId, @PathParam("username") String username){
        sessionMap.put(username, session);
        log.info("{} connected", username);
    }

    @OnClose
    public void onClose(Session session, @PathParam("questionId") String questionId, @PathParam("username") String username){
        sessionMap.remove(username);
    }

    @OnError
    public void onError(Session session, @PathParam("questionId") String questionId, @PathParam("username") String username, Throwable throwable){
        sessionMap.remove(username);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("questionId") String questionId, @PathParam("username") String username){

        managedExecutor.submit(() -> {
            AnswerEntity answerEntity = questionService.answerQuestion(UUID.fromString(questionId), new AnswerDto(username, message));
            sessionMap.values().forEach(session -> {
                session.getAsyncRemote().sendObject(answerEntity, result -> {
                    if(result.getException() != null){
                        log.error("{}", result.getException());
                    }
                });
            });
        });


    }
}
