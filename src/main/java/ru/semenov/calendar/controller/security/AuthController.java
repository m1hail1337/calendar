package ru.semenov.calendar.controller.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.semenov.calendar.db.security.user.User;
import ru.semenov.calendar.dto.security.JwtTokenDto;
import ru.semenov.calendar.dto.security.LoginRequest;
import ru.semenov.calendar.service.security.AuthService;
import ru.semenov.calendar.utils.JwtTokenUtils;


@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер аутентификации", description = "Предоставляет возможности аутентификации пользователей")
public class AuthController {
    private final AuthService authService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Авторизация в систему", description = "Выдает авторизационный JWT токен")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Аутентификация успешна"),
        @ApiResponse(responseCode = "403", description = "Неправильный логин или пароль"),
    })
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.login(), request.password()
        ));
        User user = authService.findByLogin(request.login());
        UserDetails userDetails = authService.loadUserByUsername(request.login());
        String jwt = jwtTokenUtils.generateToken(userDetails);
        authService.saveNewToken(jwt, user);
        return jwt;
    }

    @Operation(summary = "Выход из системы",
               description = "Отзывает авторизационный JWT токен: для продолжения работы клиенту необходимо получить новый")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Токен отозван"),
        @ApiResponse(responseCode = "404", description = "Токен не найден")
    })
    @PatchMapping("/logout")
    public void logout(@RequestBody JwtTokenDto token) {
        authService.revokeToken(token.jwt());
    }
}
