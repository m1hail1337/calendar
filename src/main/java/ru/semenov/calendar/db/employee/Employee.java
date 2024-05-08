package ru.semenov.calendar.db.employee;

import jakarta.persistence.*;
import lombok.*;
import ru.semenov.calendar.db.security.user.User;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Setter
@Getter
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private final String firstName;
    private final String lastName;
    private final String phoneNumber;

    @Column(name = "team_id")
    private final String teamId;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "employees_roles")
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private final Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private User user;
}
