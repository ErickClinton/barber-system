package br.com.barber.jhow.repositories;

import br.com.barber.jhow.entities.SchedulingEntity;
import br.com.barber.jhow.repositories.dto.SchedulingView;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

public interface SchedulingRepository extends JpaRepository<SchedulingEntity, Integer> {
    List<SchedulingEntity> findByBarberId(UUID id);
    Page<SchedulingView> findByUserId(UUID id, PageRequest pageable);
}
