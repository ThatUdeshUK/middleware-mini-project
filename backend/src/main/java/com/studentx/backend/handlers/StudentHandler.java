package com.studentx.backend.handlers;

import com.studentx.backend.data.StudentRepository;
import com.studentx.backend.dto.SuccessResponse;
import com.studentx.backend.models.Student;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
@RequestMapping(path = "/students")
public class StudentHandler {

    private final StudentRepository studentRepository;

    public StudentHandler(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping()
    public @ResponseBody
    Iterable<Student> getStudents() {
        return studentRepository.findAll();
    }

    @GetMapping(path = "{studentId}")
    public @ResponseBody
    Student getStudent(@PathVariable Integer studentId) {
        Optional<Student> student = studentRepository.findById(studentId);

        if (student.isPresent()) {
            return student.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public @ResponseBody
    SuccessResponse createStudent(@RequestBody Student student) {
        if (student.getName() == null || student.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        studentRepository.save(student);
        return new SuccessResponse("Student created successfully");
    }

    @PutMapping(path = "/{studentId}")
    public @ResponseBody
    SuccessResponse updateStudent(@PathVariable Integer studentId, @RequestBody Student newStudent) {
        Optional<Student> oldStudentOptional = studentRepository.findById(studentId);

        if (oldStudentOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Student oldStudent = oldStudentOptional.get();

        if (newStudent.getName() != null) {
            oldStudent.setName(newStudent.getName());
        }

        if (newStudent.getEmail() != null) {
            oldStudent.setEmail(newStudent.getEmail());
        }

        studentRepository.save(oldStudent);
        return new SuccessResponse("Student updated successfully");
    }

    @DeleteMapping(path = "{studentId}")
    public @ResponseBody
    SuccessResponse deleteStudent(@PathVariable Integer studentId) {
        boolean student = studentRepository.existsById(studentId);

        if (student) {
            studentRepository.deleteById(studentId);
            return new SuccessResponse("Student deleted successfully");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
