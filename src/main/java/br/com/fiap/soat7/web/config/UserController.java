package br.com.fiap.soat7.web.config;

import br.com.fiap.soat7.application.service.UserService;
import br.com.fiap.soat7.domain.User;
import br.com.fiap.soat7.infrastructure.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        userService.createUser(user);
        String token = jwtUtil.generateToken(user.getEmail(), null);
        return ResponseEntity.ok(token);
    }
}
