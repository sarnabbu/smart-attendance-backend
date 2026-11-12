package com.bbu.attendance.clazz.repository;

import com.bbu.attendance.clazz.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    Optional<ClassEntity> findByCode(String code);
    Optional<ClassEntity> findByInviteCode(String inviteCode);
    boolean existsByCode(String code);
    boolean existsByInviteCode(String inviteCode);

    @Query("SELECT c FROM ClassEntity c JOIN FETCH c.teacher WHERE c.teacher.id = :teacherId")
    List<ClassEntity> findByTeacherId(Long teacherId);
}
