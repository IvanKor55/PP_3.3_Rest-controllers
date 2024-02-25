package ru.javamentor.rest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = "authority")
    @Getter
    @Setter
    private String authority;

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", authority='" + authority +
                '}';
    }
}
