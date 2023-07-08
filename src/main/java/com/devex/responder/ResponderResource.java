package com.devex.responder;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.UUID;

@Path("/api/v1/responders")
@RequiredArgsConstructor
public class ResponderResource {

    final ResponderService responderService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createResponder(ResponderDto responderDto) {
        Long responderId = responderService.createResponder(responderDto);
        return Response.created(URI.create("/api/v1/responder/" + responderId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginDto loginDto) {
        responderService.login(loginDto);
        return Response.ok().build();
    }
}
