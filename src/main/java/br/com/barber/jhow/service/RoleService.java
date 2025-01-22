package br.com.barber.jhow.service;

import br.com.barber.jhow.enums.RoleEnum;
import br.com.barber.jhow.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }



}
