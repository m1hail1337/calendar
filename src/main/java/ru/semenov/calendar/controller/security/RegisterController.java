package ru.semenov.calendar.controller.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.semenov.calendar.dto.security.RegisterRequest;
import ru.semenov.calendar.service.security.RegisterService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер регистрации пользователей",
     description = "Позволяет зарегистрировать нового пользователя")
public class RegisterController {

    private final RegisterService registerService;

    @Operation(summary = "Регистрирует нового пользователя")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Пользователь создан"),
        @ApiResponse(responseCode = "409", description = "Пользователь с этим логином уже существует")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return registerService.register(request);
    }
}
