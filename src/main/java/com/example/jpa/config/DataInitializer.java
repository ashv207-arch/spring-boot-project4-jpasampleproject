package com.example.jpa.config;

import com.example.jpa.entity.Course;
import com.example.jpa.entity.Department;
import com.example.jpa.entity.Student;
import com.example.jpa.repository.CourseRepository;
import com.example.jpa.repository.DepartmentRepository;
import com.example.jpa.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(DepartmentRepository departmentRepository,
                               CourseRepository courseRepository,
                               StudentRepository studentRepository) {
        return args -> {
            if (studentRepository.count() > 0) {
                return;
            }

            Department engineering = new Department();
            engineering.setName("Engineering");
            engineering = departmentRepository.save(engineering);

            Course jpa = new Course();
            jpa.setTitle("JPA Fundamentals");
            Course spring = new Course();
            spring.setTitle("Spring Boot API");
            courseRepository.saveAll(Set.of(jpa, spring));

            Student student = new Student();
            student.setName("Alice");
            student.setEmail("alice@example.com");
            student.setFees(1200L);
            student.setDepartment(engineering);
            student.setCourses(Set.of(jpa, spring));
            studentRepository.save(student);
        };
    }
}
