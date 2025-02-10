package br.com.fiap.soat7.application.service;

import br.com.fiap.soat7.domain.Role;
import br.com.fiap.soat7.domain.User;
import br.com.fiap.soat7.domain.enums.RoleNames;
import br.com.fiap.soat7.infrastructure.repository.RoleRepository;
import br.com.fiap.soat7.infrastructure.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.fiap.soat7.infrastructure.configuration.TextReponse.USER_ID_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

	public static final String DEFAULT_PASSWORD = "1234";
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostConstruct
	public void createAdminIfNotExist() {
		List<String> allRoles = roleRepository.findAll().stream().map(Role::getName).toList();
		List<String> rolesEnum = RoleNames.fetchRoles();
		rolesEnum.stream().filter(r -> !allRoles.contains(r)).forEach(r -> roleRepository.save(new Role(r)));
		Role role = roleRepository.findByName("ADMIN");

		userRepository.findByEmail("admin@admin.com.br").orElseGet(() -> {
			User userAdmin = new User("admin@admin.com.br", bCryptPasswordEncoder.encode("adminpassword"), role);
			return userRepository.save(userAdmin);
		});
	}
	public User createUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	public User findUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException(String.format(USER_ID_NOT_FOUND, id)));
	}

	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}

	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	public User updateUser(Long id, User updatedUser) {
		User existingUser = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException(String.format(USER_ID_NOT_FOUND, id)));
		existingUser.setEmail(updatedUser.getEmail());
		existingUser.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));
		existingUser.setRole(updatedUser.getRole());
		return userRepository.save(existingUser);
	}

	public User resetPassword(String email) {

		User user = userRepository.findByEmail(email)
				.orElse(new User(email, DEFAULT_PASSWORD, null));
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRole(roleRepository.findByName("USER"));
		return userRepository.save(user);
	}
}
