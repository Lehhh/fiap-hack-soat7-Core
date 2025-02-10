package br.com.fiap.soat7.infrastructure.repository;

import br.com.fiap.soat7.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindByEmail_thenReturnUser() {
        // given
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<User> foundUser = userRepository.findByEmail("john.doe@example.com");

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void whenFindByEmail_thenReturnEmptyOptional_ifUserNotFound() {
        // when
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // then
        assertThat(foundUser).isEmpty();
    }

    @Test
    public void whenSaveUser_thenUserIsPersisted() {
        // given
        User user = new User();
        user.setEmail("jane.doe@example.com");
        user.setPassword("securePassword");

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("jane.doe@example.com");

        // Verify in the database
        Optional<User> retrievedUser = userRepository.findById(savedUser.getId());
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getEmail()).isEqualTo("jane.doe@example.com");
    }

    @Test
    public void whenDeleteUser_thenUserIsRemoved() {
        // given
        User user = new User();
        user.setEmail("delete.me@example.com");
        user.setPassword("deletePassword");
        entityManager.persist(user);
        entityManager.flush();

        // when
        userRepository.delete(user);

        // then
        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertThat(deletedUser).isEmpty();
    }

}