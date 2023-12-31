package com.devex.question;

import com.devex.responder.ResponderService;
import io.quarkus.security.UnauthorizedException;
import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Provider
@RequiredArgsConstructor
@Slf4j
public class AuthorizationFilter implements ContainerRequestFilter {

    final ResponderService responderService;
    final QuestionService questionService;

    @Context
    HttpServerRequest request;

    @Override
    public void filter(ContainerRequestContext containerRequestContext){
        String token = request.getHeader("Authorization");
        String username = request.getHeader("username");

        if(request.uri().contains("/api/v1/responders") && !containerRequestContext.getRequest().getMethod().equals("PUT")){
            return;
        }

        String[] params = request.path().split("/");
        if(params.length == 5 && questionService.isQuestionPublic(params[4])){
            return;
        }

        if(token == null || token.isBlank()|| username == null || username.isBlank()){
            throw new UnauthorizedException("Login first!");
        }
        responderService.verifyToken(UUID.fromString(token), username);
    }
}
