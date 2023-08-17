package com.devex.question;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat/questions/{questionId}/{username}", encoders = QuestionEncoder.class)
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class QuestionSocket {

    final Map<Set<String>, Session> sessionMap = new ConcurrentHashMap<>();
    final QuestionService questionService;
    final ManagedExecutor managedExecutor;

    @OnOpen
    public void onOpen(Session session, @PathParam("questionId") String questionId, @PathParam("username") String username){
        sessionMap.put(Set.of(username, questionId), session);
        log.info("{} connected", username);
    }

    @OnClose
    public void onClose(Session session, @PathParam("questionId") String questionId, @PathParam("username") String username){
        sessionMap.remove(Set.of(username, questionId));
        log.info("{} disconnected", username);

    }

    @OnError
    public void onError(Session session, @PathParam("questionId") String questionId, Throwable throwable, @PathParam("username") String username){
        sessionMap.remove(Set.of(username, questionId));
        log.info("{} error", username);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("questionId") String questionId, @PathParam("username") String username){
        log.info("questionId: {} got message: {}", questionId, message);
        String decodedUsername = URLDecoder.decode(username, StandardCharsets.UTF_8);
        managedExecutor.submit(() -> {
            AnswerEntity answerEntity = questionService.answerQuestion(UUID.fromString(questionId), decodedUsername, message);
            sessionMap.forEach((key, session) -> {
                if(key.contains(questionId)){
                    session.getAsyncRemote().sendObject(answerEntity, result -> {
                    if(result.getException() != null){
                        log.error("on message error: {}", result.getException(), result.getException());
                    }
                });
            }});
        });


    }
}
