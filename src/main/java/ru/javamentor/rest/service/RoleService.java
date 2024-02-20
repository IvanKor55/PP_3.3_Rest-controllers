package ru.javamentor.rest.service;

import ru.javamentor.rest.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    void addRole(Role role);


    Optional<Role> getRole (Long id);


    List<Role> getAllRoles();
}
