package br.com.barber.jhow.entities;

import br.com.barber.jhow.enums.TypeCutEnum;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "scheduling")
public class SchedulingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduling_id")
    private Integer id;

    @Column(name = "scheduled", nullable = false)
    private LocalDateTime scheduled;

    @Column(name = "end_service", nullable = false)
    private LocalDateTime endService;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_of_cut", nullable = false)
    private TypeCutEnum typeOfCut;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barber_id", nullable = false)
    private UserEntity barber;

    public SchedulingEntity() {
    }

    public SchedulingEntity(LocalDateTime scheduled,
                            LocalDateTime endService,
                            TypeCutEnum typeOfCut, String email, String phone, String name, UserEntity user,UserEntity barber) {
        this.scheduled = scheduled;
        this.endService = endService;
        this.typeOfCut = typeOfCut;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.user = user;
        this.barber = barber;
    }

    public SchedulingEntity(LocalDateTime scheduled,
                            LocalDateTime endService,
                            TypeCutEnum typeOfCut, String email, String phone, String name, UserEntity barber) {
        this.scheduled = scheduled;
        this.endService = endService;
        this.typeOfCut = typeOfCut;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.barber = barber;
    }


    public LocalDateTime getScheduled() {
        return scheduled;
    }

    public void setScheduled(LocalDateTime scheduled) {
        this.scheduled = scheduled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getEndService() {
        return endService;
    }

    public void setEndService(LocalDateTime endService) {
        this.endService = endService;
    }

    public TypeCutEnum getTypeOfCut() {
        return typeOfCut;
    }

    public void setTypeOfCut(TypeCutEnum typeOfCut) {
        this.typeOfCut = typeOfCut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public UserEntity getBarber() {
        return barber;
    }

    public void setBarber(UserEntity barber) {
        this.barber = barber;
    }


}
