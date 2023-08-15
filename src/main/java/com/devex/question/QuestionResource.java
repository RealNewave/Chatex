package com.devex.question;

import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.UUID;

@Path("/api/v1/questions")
@RequiredArgsConstructor
@Slf4j
public class QuestionResource {

    final QuestionService questionService;

    @Context
    HttpServerRequest request;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createQuestion(QuestionDto questionDto) {
        String username = request.getHeader("username");
        UUID questionId = questionService.createQuestion(username, questionDto);
        return Response.created(URI.create("/api/v1/questions/" + questionId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getQuestions(@QueryParam("question") String question, @QueryParam("answered") Boolean answered) {
        String username = request.getHeader("username");
        return Response.ok(questionService.getQuestions(username, question, answered)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{questionId}")
    public Response getQuestion(@PathParam("questionId") String questionId) {
        return Response.ok(questionService.getQuestion(UUID.fromString(questionId))).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{questionId}")
    public Response answerQuestion(@PathParam("questionId") String questionId, AnswerDto answerDto) {
        String username = request.getHeader("username");
        questionService.answerQuestion(UUID.fromString(questionId), username, answerDto.answer());
        return Response.accepted().build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{questionId}/close")
    public Response closeQuestion(@PathParam("questionId") String questionId){
        String username = request.getHeader("username");
        questionService.closeQuestion(UUID.fromString(questionId), username);
        return Response.accepted().build();
    }
}
