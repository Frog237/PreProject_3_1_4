package ru.kata.spring.boot_security.demo.dao;



import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserDao {

    User findById(Integer id);

    User findByUsername(String username);

    List<User> findAll();

    void update(User user);

    void delete(Integer id);

    Role findRoleById(Long roleId);

    List<Role> getAllRoles();

}
