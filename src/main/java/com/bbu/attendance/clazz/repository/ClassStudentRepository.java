package com.bbu.attendance.clazz.repository;

import com.bbu.attendance.clazz.entity.ClassStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassStudentRepository extends JpaRepository<ClassStudent, Long> {
    boolean existsByClassEntityIdAndStudentId(Long classId, Long studentId);

    @Query("SELECT cs FROM ClassStudent cs JOIN FETCH cs.student WHERE cs.classEntity.id = :classId")
    List<ClassStudent> findByClassEntityId(Long classId);
}
