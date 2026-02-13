package com.example.jpa.service;

import com.example.jpa.dto.StudentPatchRequest;
import com.example.jpa.dto.StudentRequest;
import com.example.jpa.entity.Course;
import com.example.jpa.entity.Department;
import com.example.jpa.entity.Student;
import com.example.jpa.exception.ResourceNotFoundException;
import com.example.jpa.repository.CourseRepository;
import com.example.jpa.repository.DepartmentRepository;
import com.example.jpa.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public StudentService(StudentRepository studentRepository,
                          DepartmentRepository departmentRepository,
                          CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public Student create(StudentRequest request) {
        Student student = new Student();
        applyFullRequest(student, request);
        return studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Student> findAll(String name, Pageable pageable) {
        if (name != null && !name.isBlank()) {
            return studentRepository.searchByName(name, pageable);
        }
        return studentRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Student> findByMinimumFees(Long minFees, Pageable pageable) {
        return studentRepository.findStudentsWithMinimumFees(minFees, pageable);
    }

    @Transactional(readOnly = true)
    public List<Student> findByCriteria(String name, Long minFees) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Student> query = cb.createQuery(Student.class);
        Root<Student> root = query.from(Student.class);

        Predicate predicate = cb.conjunction();
        if (name != null && !name.isBlank()) {
            predicate = cb.and(predicate, cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (minFees != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("fees"), minFees));
        }

        query.select(root).where(predicate);
        return entityManager.createQuery(query).getResultList();
    }

    @Transactional
    public Student update(Long id, StudentRequest request) {
        Student student = getById(id);
        applyFullRequest(student, request);
        return studentRepository.save(student);
    }

    @Transactional
    public Student partialUpdate(Long id, StudentPatchRequest request) {
        Student student = getById(id);

        if (request.name() != null) {
            student.setName(request.name());
        }
        if (request.email() != null) {
            student.setEmail(request.email());
        }
        if (request.fees() != null) {
            student.setFees(request.fees());
        }
        if (request.departmentId() != null) {
            student.setDepartment(resolveDepartment(request.departmentId()));
        }
        if (request.courseIds() != null) {
            student.setCourses(resolveCourses(request.courseIds()));
        }

        return studentRepository.save(student);
    }

    @Transactional
    public void delete(Long id) {
        Student student = getById(id);
        studentRepository.delete(student);
    }

    private void applyFullRequest(Student student, StudentRequest request) {
        student.setName(request.name());
        student.setEmail(request.email());
        student.setFees(request.fees());
        student.setDepartment(resolveDepartment(request.departmentId()));
        student.setCourses(resolveCourses(request.courseIds()));
    }

    private Department resolveDepartment(Long departmentId) {
        if (departmentId == null) {
            return null;
        }
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + departmentId));
    }

    private Set<Course> resolveCourses(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        List<Course> courses = courseRepository.findAllById(ids);
        if (courses.size() != ids.size()) {
            throw new ResourceNotFoundException("One or more courses were not found");
        }
        return new HashSet<>(courses);
    }
}
