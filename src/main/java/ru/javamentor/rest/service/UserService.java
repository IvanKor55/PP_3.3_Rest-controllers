package ru.javamentor.rest.service;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.javamentor.rest.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    void addUser(User user, String[] rolesList);

    void editUser(User user, String[] rolesList);

    void deleteUser(Long id);

    List<User> getListUsers();

    User getUser (Long id);

    User findByLogin(String login);
}
