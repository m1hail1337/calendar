package ru.semenov.calendar.db.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class EmployeeDbService {

    private final EmployeeRepository repository;

    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    @Transactional
    public void setManagerRoleForEmployee(String employeeId) {
        Employee employee = repository.findById(employeeId).orElseThrow(NoSuchElementException::new);
        employee.getRoles().add(Role.ROLE_MANAGER);
        repository.save(employee);
    }

    public Employee save(Employee employee) {
        return repository.save(employee);
    }
}
