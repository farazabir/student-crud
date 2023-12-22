package com.faraz.studentCrud.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private  final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public List<Student> getStudent (){
        return studentRepository.findAll();
    }

    public void addStudent(Student student) {
       Optional<Student> studentOptional =  studentRepository.findStudentByEmail(student.getEmail());
        if(studentOptional.isPresent()){
           throw new IllegalStateException("Email is already in use");
       }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
       boolean exists=studentRepository.existsById(studentId);
       if(!exists){
           throw  new IllegalStateException("Student with id "+studentId+" does not exists");
       }
       studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, Student requestStudent) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student with id " + studentId + " does not exist"));

        String newName = requestStudent.getName();
        if (newName != null && !newName.isEmpty() && !Objects.equals(student.getName(), newName)) {
            student.setName(newName);
        }

        String newEmail = requestStudent.getEmail();
        if (newEmail != null && !newEmail.isEmpty() && !Objects.equals(student.getEmail(), newEmail)) {
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(newEmail);
            if (studentOptional.isPresent()) {
                throw new IllegalStateException("Email is already in use");
            }
            student.setEmail(newEmail);
        }
    }

}
