package ru.semenov.calendar.db.task;

import jakarta.persistence.*;
import lombok.*;
import ru.semenov.calendar.db.employee.Employee;

import java.time.LocalDateTime;

@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private TaskStatus status = TaskStatus.SKETCH;

    private String message;

    @Column(name = "team_id")
    private final String teamId;

    private final LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
