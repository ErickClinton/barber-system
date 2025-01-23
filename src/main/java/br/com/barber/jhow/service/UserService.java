package br.com.barber.jhow.service;

import br.com.barber.jhow.controller.dto.LoginRequest;
import br.com.barber.jhow.controller.dto.LoginResponse;
import br.com.barber.jhow.controller.dto.SignRequest;
import br.com.barber.jhow.entities.RoleEntity;
import br.com.barber.jhow.entities.UserEntity;
import br.com.barber.jhow.repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtEncoder jwtEncoder;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtEncoder jwtEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.roleService = roleService;
    }

    public UserEntity createUser(SignRequest signRequest) {
        if (this.userRepository.findByEmail(signRequest.email()).isPresent())
            throw new RuntimeException("User already exists");

        RoleEntity roleEntity = this.roleService.getRoleEntity(signRequest.role());


        UserEntity user = new UserEntity(
                signRequest.email(),
                bCryptPasswordEncoder.encode(signRequest.password()),
                signRequest.name(),
                roleEntity
        );

        return this.userRepository.save(user);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        var user = this.userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new BadCredentialsException("User or password is invalid"));

        if (!bCryptPasswordEncoder.matches(loginRequest.password(), user.getPassword()))
            throw new BadCredentialsException("User or password is invalid");

        var now = Instant.now();
        var expiresIn = 300L;

        var scope = user.getRole().getType();

        var claims = JwtClaimsSet.builder()
                .issuer("server")
                .subject(user.getEmail())
                .expiresAt(now.plusSeconds(expiresIn))
                .issuedAt(now)
                .claim("scope", scope)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponse(jwtValue,expiresIn);
    }

    public List<UserEntity> getAllUsers() {
        return this.userRepository.findAll();
    }

}
