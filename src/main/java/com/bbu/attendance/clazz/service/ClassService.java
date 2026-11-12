package com.bbu.attendance.clazz.service;

import com.bbu.attendance.clazz.dto.*;
import java.util.List;

public interface ClassService {
    ClassResponse createClass(ClassCreateRequest request, String teacherEmail);
    ClassResponse joinClass(JoinClassRequest request, String studentEmail);
    List<ClassResponse> getMyClasses(String teacherEmail);
    List<EnrolledStudentResponse> getStudentsInClass(Long classId);
}