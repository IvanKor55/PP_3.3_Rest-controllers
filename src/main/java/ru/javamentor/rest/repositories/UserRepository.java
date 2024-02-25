package ru.javamentor.rest.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.javamentor.rest.model.User;
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.login = :login")
    User findByLogin(String login);

    @Query("SELECT MAX(id) FROM User")
    Long getLastID();
}
