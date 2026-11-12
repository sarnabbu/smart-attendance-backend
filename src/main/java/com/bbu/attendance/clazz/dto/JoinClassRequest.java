package com.bbu.attendance.clazz.dto;
import jakarta.validation.constraints.NotBlank;

public record JoinClassRequest(
    @NotBlank(message = "កូដ Invite Code មិនអាចទទេបានទេ")
    String inviteCode
) {}
