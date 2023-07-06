package com.devex;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.UUID;

@Path("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionResource {

    final QuestionService questionService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createQuestion(SubjectDto subjectDto) {
        UUID questionId = questionService.createQuestion(subjectDto);
        return Response.created(URI.create("/api/v1/questions/" + questionId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{username}")
    public Response getQuestions(@PathParam("username") String username) {
        return Response.ok(questionService.getQuestions(username)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{username}/{questionId}")
    public Response getQuestion(@PathParam("username") String username, @PathParam("questionId") String questionId) {
        return Response.ok(questionService.getQuestion(UUID.fromString(questionId))).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{questionId}")
    public Response answerQuestion(@PathParam("questionId") String questionId, AnswerDto answerDto) {
        questionService.answerQuestion(UUID.fromString(questionId), answerDto);
        return Response.accepted().build();
    }
}
