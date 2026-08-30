package com.projects.Tasks.domain.dto;

import java.util.UUID;

public record AuthResponse(String token, UUID id, String username, String email) {}