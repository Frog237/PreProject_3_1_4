package ru.kata.spring.boot_security.demo.repositories;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<User, Integer> {
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByEmail(String email);
}
