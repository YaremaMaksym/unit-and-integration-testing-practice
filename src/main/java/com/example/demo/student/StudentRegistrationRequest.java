package com.example.demo.student;

public record StudentRegistrationRequest(String name,
                                         String email,
                                         Gender gender) {
}
