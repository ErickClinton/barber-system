package br.com.barber.jhow.service;

import br.com.barber.jhow.controller.dto.AllBarbersResponse;
import br.com.barber.jhow.controller.dto.LoginRequest;
import br.com.barber.jhow.controller.dto.LoginResponse;
import br.com.barber.jhow.controller.dto.SignRequest;
import br.com.barber.jhow.entities.RoleEntity;
import br.com.barber.jhow.entities.UserEntity;
import br.com.barber.jhow.enums.RoleEnum;
import br.com.barber.jhow.exceptions.user.UserAlreadyExistsException;
import br.com.barber.jhow.exceptions.user.UserBadCredentialsException;
import br.com.barber.jhow.exceptions.user.UserNotFoundException;
import br.com.barber.jhow.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
            throw new UserAlreadyExistsException("User with email " + signRequest.email() + " already exists");

        RoleEntity roleEntity = this.roleService.getRoleEntity(signRequest.role());
        System.out.println(roleEntity.getType());

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
                .orElseThrow(() -> new UserNotFoundException("User or password is invalid"));

        if (!bCryptPasswordEncoder.matches(loginRequest.password(), user.getPassword()))
            throw new UserBadCredentialsException("User or password is invalid");

        var now = Instant.now();
        var expiresIn = 300L;

        var scope = user.getRole().getType();

        var claims = JwtClaimsSet.builder()
                .issuer("server")
                .subject(user.getId().toString())
                .expiresAt(now.plusSeconds(expiresIn))
                .issuedAt(now)
                .claim("role", scope)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponse(jwtValue,expiresIn);
    }

    public UserEntity getById(UUID id) {

        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public List<UserEntity> getAllUsers() {
        return this.userRepository.findAll();
    }

    public List<AllBarbersResponse> getAllBarbers() {
        return userRepository.findByRole_Type(RoleEnum.ADMIN)
                .stream()
                .map(barber -> new AllBarbersResponse(barber.getName()))
                .collect(Collectors.toList());
    }

}
