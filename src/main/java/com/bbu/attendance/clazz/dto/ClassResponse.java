package com.bbu.attendance.clazz.dto;
import java.time.LocalDateTime;

public record ClassResponse(
    Long id,
    String code,
    String name,
    String description,
    Long teacherId,
    String teacherName,
    String inviteCode,
    Boolean isInviteOpen,
    Integer maxStudents,
    String status,
    LocalDateTime createdAt
) {}
