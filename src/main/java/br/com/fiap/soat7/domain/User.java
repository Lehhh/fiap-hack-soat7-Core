package br.com.fiap.soat7.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user_soat7")
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;
	private String password;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn
	private List<VideoProcess> videoProcess;

	@ManyToOne
	private Role role;

	public User(String email, String password, Role role) {
		this.email = email;
		this.password = password;
		this.role = role;
	}


}
