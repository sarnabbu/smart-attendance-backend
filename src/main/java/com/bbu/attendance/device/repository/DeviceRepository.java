package com.bbu.attendance.device.repository;

import com.bbu.attendance.device.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByUserIdAndIsActiveTrue(Long userId);
    Optional<Device> findByDeviceUuid(String deviceUuid);
    boolean existsByDeviceUuid(String deviceUuid);

    @Query("SELECT d FROM Device d JOIN FETCH d.user WHERE d.user.id = :userId AND d.isActive = true")
    Optional<Device> findActiveDeviceWithUser(Long userId);
}
