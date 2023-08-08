package com.devex.responder;

import jakarta.validation.constraints.NotNull;

public record ResponderDto(@NotNull String username, @NotNull String password) {
}
