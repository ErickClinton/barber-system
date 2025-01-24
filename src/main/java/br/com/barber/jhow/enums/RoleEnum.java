package br.com.barber.jhow.enums;

import br.com.barber.jhow.exceptions.enums.RoleNotFoundException;

public enum RoleEnum {
    ADMIN,
    USER,
    BARBER;

    public static RoleEnum fromString(String role) {
        for (RoleEnum roleEnum : values()) {
            if (roleEnum.name().equalsIgnoreCase(role)) {
                return roleEnum;
            }
        }
        throw new RoleNotFoundException("Role not found: " + role);
    }
}
