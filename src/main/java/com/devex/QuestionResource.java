package com.devex;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.net.URI;

@Path("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionResource {

    final QuestionService questionService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createQuestion(SubjectDto subjectDto) {
        long questionId = questionService.createQuestion(subjectDto);
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
    public Response getQuestion(@PathParam("username") String username, @PathParam("questionId") Long questionId) {
        return Response.ok(questionService.getQuestion(questionId)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{questionId}")
    public Response answerQuestion(@PathParam("questionId") Long questionId, AnswerDto answerDto) {
        questionService.answerQuestion(questionId, answerDto);
        return Response.accepted().build();
    }
}
