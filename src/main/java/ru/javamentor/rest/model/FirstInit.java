package ru.javamentor.rest.model;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.javamentor.rest.service.RoleService;

@Component
public class FirstInit implements CommandLineRunner {

    private RoleService roleService;

    public FirstInit(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) {
        if (roleService.getRole(1L) == null) {
            Role role = new Role();
            role.setAuthority("ROLE_ADMIN");
            roleService.addRole(role);
            role = new Role();
            role.setAuthority("ROLE_USER");
            roleService.addRole(role);
        }
    }
}
