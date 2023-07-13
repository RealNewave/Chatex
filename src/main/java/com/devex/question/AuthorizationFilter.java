package com.devex.question;

import com.devex.responder.ResponderService;
import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Provider
@RequiredArgsConstructor
public class AuthorizationFilter implements ContainerRequestFilter {

    final ResponderService responderService;

    @Context
    HttpServerRequest request;

    @Override
    public void filter(ContainerRequestContext containerRequestContext){
        if(request.uri().contains("/api/v1/responders")){
            return;
        }
        String token = request.getHeader("Authorization");
        String username = request.getHeader("username");
        responderService.verifyToken(UUID.fromString(token), username);
    }
}
