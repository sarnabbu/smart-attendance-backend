package com.bbu.attendance.clazz.controller;

import com.bbu.attendance.clazz.dto.*;
import com.bbu.attendance.clazz.service.ClassService;
import com.bbu.attendance.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<ClassResponse>> createClass(
            @Valid @RequestBody ClassCreateRequest request,
            Authentication authentication
    ) {
        ClassResponse response = classService.createClass(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("បានបង្កើតថ្នាក់រៀនដោយជោគជ័យ!", response));
    }

    @GetMapping("/my-classes")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<ClassResponse>>> getMyClasses(Authentication authentication) {
        List<ClassResponse> classes = classService.getMyClasses(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("ទាញយកបញ្ជីថ្នាក់រៀនបានជោគជ័យ!", classes));
    }

    @PostMapping("/join")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<ClassResponse>> joinClass(
            @Valid @RequestBody JoinClassRequest request,
            Authentication authentication
    ) {
        ClassResponse response = classService.joinClass(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("បានចុះឈ្មោះចូលរៀនក្នុងថ្នាក់ដោយជោគជ័យ!", response));
    }

    @GetMapping("/{classId}/students")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<EnrolledStudentResponse>>> getStudentsInClass(@PathVariable Long classId) {
        List<EnrolledStudentResponse> students = classService.getStudentsInClass(classId);
        return ResponseEntity.ok(ApiResponse.success("ទាញយកបញ្ជីសិស្សក្នុងថ្នាក់បានជោគជ័យ!", students));
    }
}