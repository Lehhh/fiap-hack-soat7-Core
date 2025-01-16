package br.com.fiap.soat7.infrastructure.repository;

import br.com.fiap.soat7.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}