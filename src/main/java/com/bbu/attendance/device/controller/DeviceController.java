package com.bbu.attendance.device.controller;

import com.bbu.attendance.device.dto.DeviceBindRequest;
import com.bbu.attendance.device.dto.DeviceResponse;
import com.bbu.attendance.device.service.DeviceService;
import com.bbu.attendance.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    // សិស្សចងភ្ជាប់ទូរស័ព្ទផ្ទាល់ខ្លួន
    @PostMapping("/bind")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<DeviceResponse>> bindDevice(
            @Valid @RequestBody DeviceBindRequest request,
            Authentication authentication
    ) {
        DeviceResponse response = deviceService.bindDevice(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("បានចងភ្ជាប់ឧបករណ៍ទូរស័ព្ទដោយជោគជ័យ!", response));
    }

    // សិស្សមើលព័ត៌មានទូរស័ព្ទដែលបានចងភ្ជាប់
    @GetMapping("/my-device")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<DeviceResponse>> getMyDevice(Authentication authentication) {
        DeviceResponse response = deviceService.getMyDevice(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("ទាញយកទិន្នន័យឧបករណ៍បានជោគជ័យ!", response));
    }

    // Admin ឬ Teacher ធ្វើការ Reset Device ឱ្យសិស្សពេលប្តូរទូរស័ព្ទ (តាម Device ID)
    @PostMapping("/{deviceId}/reset")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<Void>> resetDevice(@PathVariable Long deviceId) {
        deviceService.resetDevice(deviceId);
        return ResponseEntity.ok(ApiResponse.success("បាន Reset ឧបករណ៍ដោយជោគជ័យ! សិស្សអាចចងភ្ជាប់ទូរស័ព្ទថ្មីបាន"));
    }

    // Admin ឬ Teacher ធ្វើការ Reset Device ឱ្យសិស្សពេលបាត់ទូរស័ព្ទ (តាម Student User ID)
    @PostMapping("/reset/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<Void>> resetDeviceByStudent(@PathVariable Long studentId) {
        deviceService.resetDeviceByStudentId(studentId);
        return ResponseEntity.ok(ApiResponse.success("បានកំណត់ឧបករណ៍សិស្សឡើងវិញដោយជោគជ័យ!"));
    }
}
