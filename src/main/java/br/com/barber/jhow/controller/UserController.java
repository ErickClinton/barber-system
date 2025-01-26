package br.com.barber.jhow.controller;

import br.com.barber.jhow.controller.dto.*;
import br.com.barber.jhow.entities.UserEntity;
import br.com.barber.jhow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/perfil")
    public ResponseEntity<PerfilResponse> perfil(@AuthenticationPrincipal Jwt jwt,
                                                @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        UUID id = UUID.fromString(jwt.getClaimAsString("sub"));
        var dataPerfil = this.userService.getPerfilById(id, page, pageSize);
        return ResponseEntity.ok(dataPerfil);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        var token = this.userService.login(loginRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping(path = "/sign")
    public ResponseEntity<Void> login(@RequestBody @Valid SignRequest signRequest) {
        var user = this.userService.createUser(signRequest);
        return ResponseEntity.created(URI.create("/users/"+ user.getId().toString())).build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        var users = this.userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/barbers")
    public ResponseEntity<List<AllBarbersResponse>> getAllBarbers() {
        var barbers = this.userService.getAllBarbers();
        return ResponseEntity.ok(barbers);
    }

}
