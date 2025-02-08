package br.com.fiap.soat7.infrastructure.repository;

import br.com.fiap.soat7.domain.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;


    @Test
    public void whenFindByName_thenReturnNull_ifRoleNotFound() {
        // when
        Role foundRole = roleRepository.findByName("NON_EXISTENT_ROLE");

        // then
        assertThat(foundRole).isNull();
    }

    @Test
    public void whenSaveRole_thenRoleIsPersisted() {
        // given
        Role role = new Role();
        role.setName("USER");

        // when
        Role savedRole = roleRepository.save(role);

        // then
        assertThat(savedRole.getId()).isNotNull();
        assertThat(savedRole.getName()).isEqualTo("USER");

        // Verify in the database
        Role retrievedRole = entityManager.find(Role.class, savedRole.getId());
        assertThat(retrievedRole).isNotNull();
        assertThat(retrievedRole.getName()).isEqualTo("USER");
    }

    @Test
    public void whenDeleteRole_thenRoleIsRemoved() {
        // given
        Role role = new Role();
        role.setName("TO_DELETE");
        entityManager.persist(role);
        entityManager.flush();

        // when
        roleRepository.delete(role);

        // then
        Role deletedRole = entityManager.find(Role.class, role.getId());
        assertThat(deletedRole).isNull();
    }

    @Test
    public void whenSaveRoleWithNullName_thenThrowsException() {
        // given
        Role role = new Role();
        role.setName(null);

        // when + then
        assertThrows(DataIntegrityViolationException.class, () -> {
            roleRepository.saveAndFlush(role); // Using saveAndFlush to force immediate persistence
        });
    }


}