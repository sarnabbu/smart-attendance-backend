package com.bbu.attendance.clazz.service.impl;

import com.bbu.attendance.clazz.dto.*;
import com.bbu.attendance.clazz.entity.ClassEntity;
import com.bbu.attendance.clazz.entity.ClassStudent;
import com.bbu.attendance.clazz.repository.ClassRepository;
import com.bbu.attendance.clazz.repository.ClassStudentRepository;
import com.bbu.attendance.clazz.service.ClassService;
import com.bbu.attendance.entity.User;
import com.bbu.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final ClassStudentRepository classStudentRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ClassResponse createClass(ClassCreateRequest request, String teacherEmail) {
        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new RuntimeException("រកមិនឃើញគណនីគ្រូបង្រៀន!"));

        if (classRepository.existsByCode(request.code())) {
            throw new RuntimeException("កូដថ្នាក់រៀន " + request.code() + " មានរួចហើយ!");
        }

        String inviteCode;
        do {
            inviteCode = generateInviteCode();
        } while (classRepository.existsByInviteCode(inviteCode));

        ClassEntity clazz = ClassEntity.builder()
                .code(request.code().toUpperCase().trim())
                .name(request.name().trim())
                .description(request.description())
                .teacher(teacher)
                .inviteCode(inviteCode)
                .maxStudents(request.maxStudents() != null ? request.maxStudents() : 60)
                .isInviteOpen(true)
                .status("ACTIVE")
                .build();

        return mapToResponse(classRepository.save(clazz));
    }

    @Override
    @Transactional
    public ClassResponse joinClass(JoinClassRequest request, String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("រកមិនឃើញគណនីសិស្ស!"));

        ClassEntity clazz = classRepository.findByInviteCode(request.inviteCode().toUpperCase().trim())
                .orElseThrow(() -> new RuntimeException("កូដ Invite Code មិនត្រឹមត្រូវឡើយ!"));

        if (!clazz.getIsInviteOpen()) {
            throw new RuntimeException("ថ្នាក់រៀននេះបានបិទការទទួលសិស្សថ្មីហើយ!");
        }

        if (classStudentRepository.existsByClassEntityIdAndStudentId(clazz.getId(), student.getId())) {
            throw new RuntimeException("អ្នកបានចុះឈ្មោះក្នុងថ្នាក់នេះរួចរាល់ហើយ!");
        }

        ClassStudent cs = ClassStudent.builder()
                .classEntity(clazz)
                .student(student)
                .status("ENROLLED")
                .build();
        classStudentRepository.save(cs);
        return mapToResponse(clazz);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassResponse> getMyClasses(String teacherEmail) {
        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new RuntimeException("រកមិនឃើញគណនី!"));
        return classRepository.findByTeacherId(teacher.getId()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrolledStudentResponse> getStudentsInClass(Long classId) {
        return classStudentRepository.findByClassEntityId(classId).stream()
                .map(cs -> new EnrolledStudentResponse(
                        cs.getStudent().getId(),
                        cs.getStudent().getName(),
                        cs.getStudent().getEmail(),
                        cs.getStudent().getStudentCode(),
                        cs.getEnrolledAt()
                ))
                .toList();
    }

    private String generateInviteCode() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder("BBU-");
        for (int i = 0; i < 4; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private ClassResponse mapToResponse(ClassEntity entity) {
        return new ClassResponse(
                entity.getId(), entity.getCode(), entity.getName(), entity.getDescription(),
                entity.getTeacher().getId(), entity.getTeacher().getName(),
                entity.getInviteCode(), entity.getIsInviteOpen(), entity.getMaxStudents(),
                entity.getStatus(), entity.getCreatedAt()
        );
    }
}