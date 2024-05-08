package ru.semenov.calendar.db.security.token;

import jakarta.persistence.*;
import lombok.*;
import ru.semenov.calendar.db.security.user.User;

import java.time.LocalDateTime;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@ToString
@Table(name = "auth_tokens")
public class Token {
    @Id
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType type;
    
    private LocalDateTime creationTime;
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected Token() { }
}
