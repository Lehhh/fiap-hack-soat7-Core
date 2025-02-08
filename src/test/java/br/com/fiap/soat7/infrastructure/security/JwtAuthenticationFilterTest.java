package br.com.fiap.soat7.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Test
    public void whenDoFilterInternal_withValidToken_thenAuthenticationIsSet() throws ServletException, IOException {
        // Given
        String token = "validToken";
        String username = "test@example.com";
        String header = "Bearer " + token;
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, "password", new ArrayList<>());

        when(request.getHeader("Authorization")).thenReturn(header);
        when(jwtUtil.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtil.validateToken(token, username)).thenReturn(true);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, chain);

        // Then
        verify(chain).doFilter(request, response);
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        assert authentication.getPrincipal().equals(userDetails);

        // Cleanup (optional, but good practice to avoid state leaking between tests)
        SecurityContextHolder.clearContext();
    }

    @Test
    public void whenDoFilterInternal_withNoToken_thenChainIsCalledAndAuthenticationIsNotSet() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn(null);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, chain);

        // Then
        verify(chain).doFilter(request, response);
        assert SecurityContextHolder.getContext().getAuthentication() == null;

        // Cleanup
        SecurityContextHolder.clearContext();
    }

    @Test
    public void whenDoFilterInternal_withInvalidToken_thenChainIsCalledAndAuthenticationIsNotSet() throws ServletException, IOException {
        // Given
        String token = "invalidToken";
        String header = "Bearer " + token;
        String username = "test@example.com";
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, "password", new ArrayList<>());

        when(request.getHeader("Authorization")).thenReturn(header);
        when(jwtUtil.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtil.validateToken(token, username)).thenReturn(false); // Token validation fails

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, chain);

        // Then
        verify(chain).doFilter(request, response);
        assert SecurityContextHolder.getContext().getAuthentication() == null;

        // Cleanup
        SecurityContextHolder.clearContext();
    }

    @Test
    public void whenDoFilterInternal_withInvalidHeaderFormat_thenChainIsCalledAndAuthenticationIsNotSet() throws ServletException, IOException {
        // Given
        String header = "InvalidFormatToken"; // Header does not start with "Bearer "
        when(request.getHeader("Authorization")).thenReturn(header);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, chain);

        // Then
        verify(chain).doFilter(request, response);
        assert SecurityContextHolder.getContext().getAuthentication() == null;

        // Cleanup
        SecurityContextHolder.clearContext();
    }

    @Test
    public void whenDoFilterInternal_withUsernameNullInToken_thenChainIsCalledAndAuthenticationIsNotSet() throws ServletException, IOException {
        //Given
        String token = "validToken";
        String header = "Bearer " + token;
        when(request.getHeader("Authorization")).thenReturn(header);
        when(jwtUtil.extractUsername(token)).thenReturn(null);

        //When
        jwtAuthenticationFilter.doFilterInternal(request, response, chain);

        //Then
        verify(chain).doFilter(request, response);
        assert SecurityContextHolder.getContext().getAuthentication() == null;

        // Cleanup
        SecurityContextHolder.clearContext();
    }

}