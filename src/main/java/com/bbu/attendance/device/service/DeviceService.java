package com.bbu.attendance.device.service;

import com.bbu.attendance.device.dto.DeviceBindRequest;
import com.bbu.attendance.device.dto.DeviceResponse;

public interface DeviceService {
    DeviceResponse bindDevice(DeviceBindRequest request, String userEmail);
    DeviceResponse getMyDevice(String userEmail);
    void resetDevice(Long deviceId);
    void resetDeviceByStudentId(Long studentId);
}
