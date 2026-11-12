package com.bbu.attendance.clazz.dto;

import jakarta.validation.constraints.NotBlank;

public record ClassCreateRequest(
    @NotBlank(message = "កូដថ្នាក់មិនអាចទទេបានទេ (Class code is required)")
    String code,

    @NotBlank(message = "ឈ្មោះថ្នាក់មិនអាចទទេបានទេ (Class name is required)")
    String name,

    String description,
    Integer maxStudents
) {}