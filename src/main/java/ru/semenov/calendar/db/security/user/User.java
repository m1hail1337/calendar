package ru.semenov.calendar.db.security.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import ru.semenov.calendar.db.employee.Employee;

@Entity
@Getter
@NoArgsConstructor(force = true)
@Table(name = "users_auth")
@Builder
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private final String login;

    private final String password;

    @OneToOne
    @MapsId
    @JoinColumn(name = "employee_id")
    private final Employee employee;
}
