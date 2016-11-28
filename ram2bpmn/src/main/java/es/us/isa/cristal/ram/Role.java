package es.us.isa.cristal.ram;

/**
 * Role
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class Role {

    private String name;

    private Role() {

    }

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;

        Role role = (Role) o;

        return name.equals(role.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
