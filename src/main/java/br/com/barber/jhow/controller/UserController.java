package br.com.barber.jhow.controller;

import br.com.barber.jhow.controller.dto.LoginRequest;
import br.com.barber.jhow.controller.dto.LoginResponse;
import br.com.barber.jhow.controller.dto.SignRequest;
import br.com.barber.jhow.entities.UserEntity;
import br.com.barber.jhow.repositories.UserRepository;
import br.com.barber.jhow.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;
    private final JwtEncoder jwtEncoder;

    public UserController(UserService userService, JwtEncoder jwtEncoder) {
        this.userService = userService;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        var token = this.userService.login(loginRequest);
        System.out.println(token.accessToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping(path = "/sign")
    public ResponseEntity<Void> login(@RequestBody SignRequest signRequest) {
        var user = this.userService.createUser(signRequest);
        return ResponseEntity.created(URI.create("/users/"+ user.getId().toString())).build();
    }

}
