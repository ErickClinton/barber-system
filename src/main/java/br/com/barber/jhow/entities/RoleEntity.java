package br.com.barber.jhow.entities;

import br.com.barber.jhow.enums.RoleEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, name = "type")
    private RoleEnum type;

    public RoleEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleEnum getType() {
        return type;
    }

    public void setType(RoleEnum type) {
        this.type = type;
    }
}
