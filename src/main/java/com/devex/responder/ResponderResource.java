package com.devex.responder;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.net.URI;

@Path("/api/v1/responders")
@RequiredArgsConstructor
public class ResponderResource {

    final ResponderService responderService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response createResponder(ResponderDto responderDto) {
        Long responderId = responderService.createResponder(responderDto);
        return Response.created(URI.create("/api/v1/responder/" + responderId)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/login")
    public Response login(LoginDto loginDto) {
        return Response.accepted(responderService.login(loginDto)).build();
    }
}
