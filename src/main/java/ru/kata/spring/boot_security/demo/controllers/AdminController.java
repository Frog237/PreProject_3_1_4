package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
	public String usersList(ModelMap model, Authentication authentication) {
		User currentUser = (User) authentication.getPrincipal();
		model.addAttribute("user", currentUser);

		model.addAttribute("userRoles", currentUser.getRoles());

		List<User> users = userService.findAll();
		model.addAttribute("users", users);

		List<Role> allRoles = userService.getAllRoles();
		model.addAttribute("allRoles", allRoles);
		return "admin";
	}

	@PostMapping(value = "/admin/add")
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
	public String deleteUser(
			@RequestParam("id") Integer id) {
		userService.delete(id);
		return "redirect:/admin";
	}

	@PostMapping(value = "/admin/update")
	public String updateUser(
			@RequestParam("edit_id") Integer id,
			@RequestParam("edit_first_name") String firstName,
			@RequestParam("edit_last_name") String lastName,
			@RequestParam("edit_age") byte age,
			@RequestParam("edit_email") String email,
			@RequestParam("edit_password") String password,
			@RequestParam("edit_roles") List<Long> roleIds) {

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