package ru.semenov.calendar.db.security.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    @Query("""
                SELECT t
                FROM Token t
                WHERE t.user.login = :login AND t.revoked = false
            """)
    List<Token> findAllValidTokensByLogin(String login);

    @Query("""
                SELECT t
                FROM Token t
                WHERE t.revoked = false
            """)
    List<Token> findAllNotRevokedTokens();
}
