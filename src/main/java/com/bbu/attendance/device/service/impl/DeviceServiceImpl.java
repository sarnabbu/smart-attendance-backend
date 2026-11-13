package com.bbu.attendance.device.service.impl;

import com.bbu.attendance.device.dto.DeviceBindRequest;
import com.bbu.attendance.device.dto.DeviceResponse;
import com.bbu.attendance.device.entity.Device;
import com.bbu.attendance.device.repository.DeviceRepository;
import com.bbu.attendance.device.service.DeviceService;
import com.bbu.attendance.entity.User;
import com.bbu.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public DeviceResponse bindDevice(DeviceBindRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("រកមិនឃើញគណនីអ្នកប្រើប្រាស់!"));

        String reqUuid = request.deviceUuid().trim();

        // 1. ពិនិត្យមើលថាតើសិស្សមានចងទូរស័ព្ទណាមួយរួចហើយឬនៅ (1 Student = 1 Phone)
        Optional<Device> existingUserDevice = deviceRepository.findByUserIdAndIsActiveTrue(user.getId());
        if (existingUserDevice.isPresent()) {
            Device current = existingUserDevice.get();
            if (current.getDeviceUuid().equalsIgnoreCase(reqUuid)) {
                return mapToResponse(current); // ទូរស័ព្ទដដែល
            } else {
                throw new RuntimeException("គណនីនេះបានភ្ជាប់ជាមួយទូរស័ព្ទផ្សេងរួចហើយ! មិនអាចប្តូរទូរស័ព្ទដោយគ្មានការអនុញ្ញាតពី Admin/Teacher ឡើយ (Anti-Buddy Punching)");
            }
        }

        // 2. ពិនិត្យមើលថាតើទូរស័ព្ទនេះត្រូវបានសិស្សផ្សេងចងភ្ជាប់រួចហើយឬនៅ (1 Phone cannot be shared by multiple students)
        Optional<Device> deviceWithUuid = deviceRepository.findByDeviceUuid(reqUuid);
        if (deviceWithUuid.isPresent() && deviceWithUuid.get().getIsActive()) {
            throw new RuntimeException("ទូរស័ព្ទនេះត្រូវបានចុះឈ្មោះដោយសិស្សផ្សេងរួចហើយ! មិនអាចប្រើទូរស័ព្ទរួមគ្នាបានឡើយ");
        }

        Device newDevice = Device.builder()
                .user(user)
                .deviceUuid(reqUuid)
                .deviceName(request.deviceName().trim())
                .platform(request.platform().toUpperCase().trim())
                .isActive(true)
                .build();

        Device saved = deviceRepository.save(newDevice);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DeviceResponse getMyDevice(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("រកមិនឃើញគណនី!"));

        Device device = deviceRepository.findByUserIdAndIsActiveTrue(user.getId())
                .orElseThrow(() -> new RuntimeException("គណនីនេះមិនទាន់បានចងភ្ជាប់ទូរស័ព្ទណាមួយនៅឡើយទេ"));

        return mapToResponse(device);
    }

    @Override
    @Transactional
    public void resetDevice(Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("រកមិនឃើញទិន្នន័យ Device!"));

        device.setIsActive(false);
        deviceRepository.save(device);
    }

    @Override
    @Transactional
    public void resetDeviceByStudentId(Long studentId) {
        Device device = deviceRepository.findByUserIdAndIsActiveTrue(studentId)
                .orElseThrow(() -> new RuntimeException("រកមិនឃើញទិន្នន័យ Device របស់សិស្សនេះ!"));

        device.setIsActive(false);
        deviceRepository.save(device);
    }

    private DeviceResponse mapToResponse(Device device) {
        return new DeviceResponse(
                device.getId(),
                device.getUser().getId(),
                device.getUser().getName(),
                device.getUser().getStudentCode(),
                device.getDeviceUuid(),
                device.getDeviceName(),
                device.getPlatform(),
                device.getIsActive(),
                device.getRegisteredAt(),
                device.getLastUsedAt()
        );
    }
}
