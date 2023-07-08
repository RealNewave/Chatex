package com.devex.responder;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class ResponderService {

    @Transactional
    public Long createResponder(ResponderDto responderDto) {
        ResponderEntity responderEntity = new ResponderEntity();
        responderEntity.setUsername(responderDto.username());
        responderEntity.setPassword(responderDto.password());
        ResponderEntity.persist(responderEntity);
        return responderEntity.id;
    }

    public void login(LoginDto loginDto) {
        ResponderEntity.find("username = ?1 AND password = ?2", loginDto.username(), loginDto.password())
                .firstResultOptional()
                .orElseThrow(NotFoundException::new);
    }
}
