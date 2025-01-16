package br.com.fiap.soat7.application.service;

import br.com.fiap.soat7.domain.Role;
import br.com.fiap.soat7.domain.User;
import br.com.fiap.soat7.domain.enums.RoleNames;
import br.com.fiap.soat7.infrastructure.repository.RoleRepository;
import br.com.fiap.soat7.infrastructure.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

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
		return userRepository.save(user);
	}
}
