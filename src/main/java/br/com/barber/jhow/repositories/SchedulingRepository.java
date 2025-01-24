package br.com.barber.jhow.repositories;

import br.com.barber.jhow.entities.SchedulingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SchedulingRepository extends JpaRepository<SchedulingEntity, Integer> {
    List<SchedulingEntity> findByBarberId(UUID id);
}
