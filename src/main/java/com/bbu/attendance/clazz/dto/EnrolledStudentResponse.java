package com.bbu.attendance.clazz.dto;
import java.time.LocalDateTime;

public record EnrolledStudentResponse(
    Long studentId,
    String studentName,
    String email,
    String studentCode,
    LocalDateTime enrolledAt
) {}
