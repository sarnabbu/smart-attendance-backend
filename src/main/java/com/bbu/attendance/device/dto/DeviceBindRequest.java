package com.bbu.attendance.device.dto;

import jakarta.validation.constraints.NotBlank;

public record DeviceBindRequest(
    @NotBlank(message = "Device UUID មិនអាចទទេបានទេ (Device UUID is required)")
    String deviceUuid,

    @NotBlank(message = "Device Name មិនអាចទទេបានទេ (e.g. iPhone 15 Pro, Samsung S24)")
    String deviceName,

    @NotBlank(message = "Platform មិនអាចទទេបានទេ (iOS ឬ Android)")
    String platform
) {}
