package com.projects.Tasks.domain.dto;

public record LoginRequest(
    String email,
    String password
) {
}
