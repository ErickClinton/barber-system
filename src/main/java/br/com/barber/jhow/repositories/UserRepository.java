package br.com.barber.jhow.repositories;

import br.com.barber.jhow.entities.UserEntity;
import br.com.barber.jhow.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByRole_Type(RoleEnum roleType);
}
