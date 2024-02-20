package ru.javamentor.rest.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.javamentor.rest.model.Role;
import ru.javamentor.rest.model.User;
import ru.javamentor.rest.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void addUser(User user, String[] rolesList) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
        if (rolesList != null) {
            List<Role> roles = Arrays.stream(rolesList)
                    .map(roleId -> roleService.getRole(Long.parseLong(roleId)).get())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (!roles.isEmpty()) {
                user.setRoles(roles);
            }
        }
    }

    @Transactional
    @Override
    public void editUser(User user, String[] rolesList) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (rolesList != null) {
            List<Role> roles = new ArrayList<>();
            for (String roleId : rolesList) {
                Role role = roleService.getRole(Long.parseLong(roleId)).get();
                roles.add(role);
            }
            if (!roles.isEmpty()) {
                user.setRoles(roles);
            }
        }
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getListUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User getUser (Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        } else {
            return new UserDetailsImpl(user);
        }
    }
}