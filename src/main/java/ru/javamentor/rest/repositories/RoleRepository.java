package ru.javamentor.rest.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.javamentor.rest.model.Role;

@Repository
public interface RoleRepository  extends CrudRepository<Role, Long> {
}
