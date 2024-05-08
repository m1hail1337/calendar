package ru.semenov.calendar.service.security;

import java.util.*;

import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import ru.semenov.calendar.db.employee.Employee;
import ru.semenov.calendar.db.employee.EmployeeDbService;
import ru.semenov.calendar.db.security.user.User;
import ru.semenov.calendar.db.security.user.UserDbService;
import ru.semenov.calendar.dto.security.RegisterRequest;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final UserDbService userDbService;
    private final EmployeeDbService employeeService;

    @Transactional
    public String register(RegisterRequest request) {
        checkLogin(request.login());

        Employee employee = Employee.builder()
            .firstName(request.firstName())
            .lastName(request.lastName())
            .phoneNumber(request.phoneNumber())
            .build();
        Employee savedEmployee = employeeService.save(employee);
        User user = buildUserFromRequest(request, savedEmployee);
        userDbService.save(user);
        return savedEmployee.getId();
    }

    private User buildUserFromRequest(RegisterRequest request, Employee employee) {
        return User.builder()
            .login(request.login())
            .password(request.password())
            .employee(employee).build();
    }

    private void checkLogin(String login) {
        Optional<User> dbUser = userDbService.findByLogin(login);
        if (dbUser.isPresent()) {
            throw new EntityExistsException("Пользователь с логином " + login + " уже существует");
        }
    }
}
