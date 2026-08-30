package com.projects.Tasks.domain.dto;

public record ErrorResponse(
        int status,
        String message,
        String details
        )
        {}
