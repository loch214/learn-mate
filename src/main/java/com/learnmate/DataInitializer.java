package com.learnmate;

import com.learnmate.model.User;
import com.learnmate.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsernameOrEmail("admin", "admin@learnmate.com").isEmpty()) {
            User admin = new User();
            admin.setFullName("Admin User");
            admin.setUsername("admin");
            admin.setEmail("admin@learnmate.com");
            admin.setPassword(passwordEncoder.encode("adminpass"));
            admin.setRole("ROLE_ADMIN");
            admin.setDetails("System Administrator");
            userRepository.save(admin);
        }

        if (userRepository.findByUsernameOrEmail("teacher", "teacher@learnmate.com").isEmpty()) {
            User teacher = new User();
            teacher.setFullName("Jane Doe");
            teacher.setUsername("teacher");
            teacher.setEmail("teacher@learnmate.com");
            teacher.setPassword(passwordEncoder.encode("teacherpass"));
            teacher.setRole("ROLE_TEACHER");
            teacher.setDetails("Mathematics");
            userRepository.save(teacher);
        }

        if (userRepository.findByUsernameOrEmail("student", "student@learnmate.com").isEmpty()) {
            User student = new User();
            student.setFullName("John Smith");
            student.setUsername("student");
            student.setEmail("student@learnmate.com");
            student.setPassword(passwordEncoder.encode("studentpass"));
            student.setRole("ROLE_STUDENT");
            student.setDetails("Grade 10");
            userRepository.save(student);
        }
    }
}