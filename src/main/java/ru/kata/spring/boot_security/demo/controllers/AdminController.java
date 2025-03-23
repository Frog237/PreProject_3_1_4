package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class AdminController {

	private final UserService userService;

	@Autowired
	public AdminController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(value = "/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String usersList(ModelMap model) {
		List<User> users = userService.findAll();
		model.addAttribute("users", users);

		List<Role> allRoles = userService.getAllRoles();
		model.addAttribute("allRoles", allRoles);
		return "admin";
	}

	@PostMapping(value = "/admin/add")
	@PreAuthorize("hasRole('ADMIN')")
	public String addUser(
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName,
			@RequestParam("age") byte age,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("roleIds") List<Long> roleIds) {
		User addedUser = new User(firstName, lastName, age, email, password);
		Set<Role> roles = roleIds.stream()
				.map(roleId -> userService.findRoleById(roleId))
				.collect(Collectors.toSet());
		addedUser.setRoles(roles);
		userService.add(addedUser);
		return "redirect:/admin";
	}

	@PostMapping(value = "/admin/delete")
	@PreAuthorize("hasRole('ADMIN')")
	public String deleteUser(
			@RequestParam("id") Integer id) {
		userService.delete(id);
		return "redirect:/admin";
	}

	@GetMapping(value = "/admin/edit")
	@PreAuthorize("hasRole('ADMIN')")
	public String editUserForm(@RequestParam("id") Integer id, ModelMap model) {
		User user = userService.findById(id);
		model.addAttribute("user", user);

		List<Role> allRoles = userService.getAllRoles();
		model.addAttribute("allRoles", allRoles);

		return "user-editor";
	}

	@PostMapping(value = "/admin/update")
	@PreAuthorize("hasRole('ADMIN')")
	public String updateUser(
			@RequestParam("id") Integer id,
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName,
			@RequestParam("age") byte age,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("roleIds") List<Long> roleIds) {

		User updatedUser = userService.findById(id);
		updatedUser.setFirstName(firstName);
		updatedUser.setLastName(lastName);
		updatedUser.setAge(age);
		updatedUser.setEmail(email);
		updatedUser.setPassword(password);
		Set<Role> roles = roleIds.stream()
				.map(roleId -> userService.findRoleById(roleId))
				.collect(Collectors.toSet());
		updatedUser.setRoles(roles);

		userService.update(updatedUser);
		return "redirect:/admin";
	}

}