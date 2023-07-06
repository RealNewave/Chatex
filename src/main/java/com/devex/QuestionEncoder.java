package com.devex;

import jakarta.websocket.*;

public class QuestionEncoder implements Encoder.Text<AnswerEntity>{

    @Override
    public String encode(AnswerEntity answerEntity) {
        return answerEntity.toString();
    }

    @Override
    public void init(EndpointConfig config) {
        Text.super.init(config);
    }

    @Override
    public void destroy() {
        Text.super.destroy();
    }
}
