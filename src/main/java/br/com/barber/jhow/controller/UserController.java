package br.com.barber.jhow.controller;

import br.com.barber.jhow.entities.UserEntity;
import br.com.barber.jhow.repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private final UserRepository userEntity;

    public UserController(UserRepository userEntity) {
        this.userEntity = userEntity;
    }

    @GetMapping()
    public String teste(){
        var newUser = new UserEntity();
        newUser.setName("John Doe");
        return "d";
    }
}
