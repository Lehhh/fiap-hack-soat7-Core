package br.com.fiap.soat7.infrastructure.security;

import br.com.fiap.soat7.domain.Role;
import br.com.fiap.soat7.domain.User;
import br.com.fiap.soat7.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public void whenLoadUserByUsername_thenReturnUserDetails() {
        // given
        String email = "test@example.com";
        String password = "password";
        Role role = new Role();
        role.setName("USER");

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        // then
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isEqualTo(password);
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");  // Spring Security adiciona o prefixo "ROLE_"
    }

    @Test
    public void whenLoadUserByUsername_thenRoleNameIsConvertedToUpperCase() {
        // given
        String email = "test@example.com";
        String password = "password";
        Role role = new Role();
        role.setName("user");  // Role name em lowercase

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        // then
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER"); // Deve estar em uppercase
    }


    @Test
    public void whenLoadUserByUsername_thenThrowsException_ifUserNotFound() {
        // given
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when + then
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(email);
        });
    }
}