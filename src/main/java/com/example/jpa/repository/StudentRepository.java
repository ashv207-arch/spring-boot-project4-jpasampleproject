package com.example.jpa.repository;

import com.example.jpa.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    @Query("select s from Student s where lower(s.name) like lower(concat('%', :name, '%'))")
    Page<Student> searchByName(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT * FROM students s WHERE s.fees >= :minFees", nativeQuery = true)
    Page<Student> findStudentsWithMinimumFees(@Param("minFees") Long minFees, Pageable pageable);
}
