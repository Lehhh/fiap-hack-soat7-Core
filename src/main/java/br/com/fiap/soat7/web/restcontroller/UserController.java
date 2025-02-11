package br.com.fiap.soat7.web.restcontroller;

import br.com.fiap.soat7.application.service.UserService;
import br.com.fiap.soat7.domain.User;
import br.com.fiap.soat7.domain.VideoProcess;
import br.com.fiap.soat7.infrastructure.security.JwtUtil;
import ch.qos.logback.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static br.com.fiap.soat7.infrastructure.configuration.TextReponse.MESSAGE;

@Controller
@RequestMapping("/users")
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

    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserDetails(@PathVariable Long id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reset-password")
    public String resetPassword(String username, Model model) {
        if (!StringUtil.isNullOrEmpty(username)) {
            userService.resetPassword(username);
            model.addAttribute(MESSAGE,  UserService.DEFAULT_PASSWORD);
        }
        return "user/reset-password";
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<VideoProcess>> getVideoStatusById(@PathVariable Long id) {
        List<VideoProcess> videosStatus = userService.getVideoStatusesById(id);
        return ResponseEntity.ok(videosStatus);
    }
}
