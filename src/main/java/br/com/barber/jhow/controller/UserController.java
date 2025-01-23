package br.com.barber.jhow.controller;

import br.com.barber.jhow.controller.dto.LoginRequest;
import br.com.barber.jhow.controller.dto.LoginResponse;
import br.com.barber.jhow.controller.dto.SignRequest;
import br.com.barber.jhow.entities.UserEntity;
import br.com.barber.jhow.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        var token = this.userService.login(loginRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping(path = "/sign")
    public ResponseEntity<Void> login(@RequestBody SignRequest signRequest) {
        var user = this.userService.createUser(signRequest);
        return ResponseEntity.created(URI.create("/users/"+ user.getId().toString())).build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        var users = this.userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

}
