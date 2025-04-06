package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    void add(User user);

    User findById(Integer id);

    User findByEmail(String email);

    Set<Role> findRolesByIds(List<Long> roleIds);

    List<User> findAll();

    void update(User user);

    void delete(Integer id);

    Role findRoleById(Long roleId);

    List<Role> getAllRoles();

}
