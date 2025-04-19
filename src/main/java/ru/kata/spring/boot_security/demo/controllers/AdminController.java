package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private final UserService userService;
	private final RoleRepository roleRepository;

	@Autowired
	public AdminController(UserService userService, RoleRepository roleRepository) {
		this.userService = userService;
		this.roleRepository = roleRepository;
	}

	@GetMapping
	public String usersList(ModelMap model, Authentication authentication) {
		User currentUser = (User) authentication.getPrincipal();
		model.addAttribute("user", currentUser);
		model.addAttribute("userRoles", userService.getSortedRoles(currentUser));
		return "admin";
	}

//	@PostMapping(value = "/api/users")
//	public ResponseEntity<User> addUser(@RequestBody User user) {
//		// Здесь предполагается, что роли приходят как List<Role>
//		user.setRoles(user.getRoles().stream()
//				.map(role -> userService.findRoleById(role.getId()))
//				.collect(Collectors.toSet()));
//		userService.add(user);
//		return ResponseEntity.ok(user);
//	}
//
//
//	@DeleteMapping(value = "/{id}")
//	public String deleteUser(
//			@PathVariable("id") Integer id) {
//		userService.delete(id);
//		return "redirect:/admin";
//	}
//
//	@PatchMapping(value = "/{id}")
//	public String updateUser(@ModelAttribute("user") User user,
//							 @PathVariable("id") Integer id) {
//		userService.update(user, id);
//		return "redirect:/admin";
//	}

}