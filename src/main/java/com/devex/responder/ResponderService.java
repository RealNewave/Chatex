package com.devex.responder;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class ResponderService {

    @Transactional
    public Long createResponder(ResponderDto responderDto) {
        ResponderEntity responderEntity = new ResponderEntity();
        responderEntity.setUsername(responderDto.username());
        responderEntity.setPassword(BcryptUtil.bcryptHash(responderDto.password()));
        ResponderEntity.persist(responderEntity);
        return responderEntity.id;
    }

    @Transactional
    public UUID login(LoginDto loginDto) {
        Optional<ResponderEntity> responderEntity = ResponderEntity
                .find("username = ?1", loginDto.username())
                .firstResultOptional();

        if(responderEntity.isEmpty() || !BcryptUtil.matches(loginDto.password(), responderEntity.get().getPassword())){
            throw new NotFoundException();
        }
        ResponderTokenEntity responderTokenEntity = new ResponderTokenEntity();
        responderTokenEntity.persist();
        responderEntity.get().setToken(responderTokenEntity);
        responderEntity.get().persist();
        return responderTokenEntity.getToken();
    }

    @Transactional
    public void verifyToken(UUID token, String username){
        Optional<ResponderTokenEntity> responderTokenEntityOptional = ResponderTokenEntity.findByIdOptional(token);
        if(responderTokenEntityOptional.isEmpty()){
            throw new UnauthorizedException("You should login first");
        }
        ResponderTokenEntity responderTokenEntity = responderTokenEntityOptional.get();
        boolean isTokenUsernameMatch = ResponderEntity.find("username = ?1 and token.token = ?2", username, token).firstResultOptional().isPresent();
        boolean isValid = isTokenUsernameMatch && responderTokenEntity.getUpdated().isBefore(ZonedDateTime.now().plusMinutes(30L));
        if(isValid){
            responderTokenEntity.setUpdated(ZonedDateTime.now());
            responderTokenEntity.persist();
            return;
        }
        throw new UnauthorizedException("Token expired");
    }
}
