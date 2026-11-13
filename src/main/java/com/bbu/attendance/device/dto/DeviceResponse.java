package com.bbu.attendance.device.dto;

import java.time.LocalDateTime;

public record DeviceResponse(
    Long id,
    Long userId,
    String userName,
    String studentCode,
    String deviceUuid,
    String deviceName,
    String platform,
    Boolean isActive,
    LocalDateTime registeredAt,
    LocalDateTime lastUsedAt
) {}
