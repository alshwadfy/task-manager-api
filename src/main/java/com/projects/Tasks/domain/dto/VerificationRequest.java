package com.projects.Tasks.domain.dto;

public record VerificationRequest(
    String email,
    String code
) {}