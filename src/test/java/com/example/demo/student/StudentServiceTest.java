package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }

    @Test
    void canGetAllStudents() {
        // testing if repository method was invoked

        // when
        underTest.getAllStudents();
        // then
        verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        // testing if argument is the same as in repository method

        // given
        StudentRegistrationRequest request = new StudentRegistrationRequest("Jamila", "jamila@gmail.com", Gender.FEMALE);

        Student student = new Student(request.name(), request.email(), request.gender());

        // when
        underTest.addStudent(request);

        // then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // given
        StudentRegistrationRequest request = new StudentRegistrationRequest("Jamila", "jamila@gmail.com", Gender.FEMALE);

        given(studentRepository.selectExistsEmail(anyString())).willReturn(true);

        // when

        // then
        assertThatThrownBy(() -> underTest.addStudent(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + request.email() + " taken");

        //never executing
        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteStudent() {
        // given
        Long studentId = 10L;

        given(studentRepository.existsById(any())).willReturn(true);

        // when
        underTest.deleteStudent(studentId);

        // then
        ArgumentCaptor<Long> captureStudentId = ArgumentCaptor.forClass(Long.class);

        verify(studentRepository).deleteById(captureStudentId.capture());

        assertThat(captureStudentId.getValue()).isEqualTo(studentId);
    }

    @Test
    void willThrowWhenStudentNotExists() {
        // given
        Long studentId = 10L;

        given(studentRepository.existsById(any())).willReturn(false);
        // when

        // then
        assertThatThrownBy(() -> underTest.deleteStudent(studentId))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + studentId + " does not exists");

        verify(studentRepository, never()).deleteById(any());
    }
}