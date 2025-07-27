package pe.gob.casadelaliteratura.biblioteca.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.login.LoginRequest;
import pe.gob.casadelaliteratura.biblioteca.dtos.login.LoginResponse;
import pe.gob.casadelaliteratura.biblioteca.services.impl.login.LoginService;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(loginService.login(request));
    }

}

