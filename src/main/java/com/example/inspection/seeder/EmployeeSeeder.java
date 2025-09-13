package com.example.inspection.seeder;

import com.example.inspection.entity.Employee;
import com.example.inspection.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeSeeder implements CommandLineRunner {

    @Value("${custom.data.employee.fullName}")
    private String fullName;
    @Value("${custom.data.employee.username}")
    private String username;
    @Value("${custom.data.employee.password}")
    private String password;
    @Value("${custom.data.employee.email}")
    private String email;
    @Value("${custom.data.employee.position}")
    private String position;
    @Value("${custom.data.employee.phone}")
    private String phone;
    @Value("${custom.data.employee.note}")
    private String note;
    @Value("${custom.data.employee.dob}")
    private String dob;

    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if employees already exist
        if (employeeRepository.count() == 0) {
            // Create test employee
            Employee testEmployee = new Employee();
            testEmployee.setFullName(fullName);
            testEmployee.setUsername(username);
            testEmployee.setEmail(email);
            testEmployee.setPasswordHash(password);
            testEmployee.setPosition(Employee.Position.valueOf(position));
            testEmployee.setPhone(phone);
            testEmployee.setNote(note);
            testEmployee.setDob(LocalDate.parse(dob));

            employeeRepository.save(testEmployee);
        }
    }
}
