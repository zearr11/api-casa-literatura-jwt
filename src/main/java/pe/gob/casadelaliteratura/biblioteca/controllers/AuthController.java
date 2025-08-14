package pe.gob.casadelaliteratura.biblioteca.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.casadelaliteratura.biblioteca.dtos.auth.AuthRequest;
import pe.gob.casadelaliteratura.biblioteca.dtos.auth.AuthResponse;
import pe.gob.casadelaliteratura.biblioteca.dtos.auth.RefreshResponse;
import pe.gob.casadelaliteratura.biblioteca.services.impl.auth.AuthService;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // http://localhost:8080/api/v1/auth
    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(request));
    }

    // http://localhost:8080/api/v1/refresh
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@RequestParam String refreshToken) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.refresh(refreshToken));
    }

}

