package br.com.barber.jhow.repositories;

import br.com.barber.jhow.entities.RoleEntity;
import br.com.barber.jhow.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
     Optional<RoleEntity> findByType(RoleEnum type);
}
