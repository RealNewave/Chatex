package com.devex.question;

import java.util.List;

public record QuestionDto(String question, List<String> usernames) {

}
