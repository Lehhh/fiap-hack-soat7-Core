package br.com.fiap.soat7.infrastructure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

	private static final String SECRET_KEY = generateRandomSecretKey(32);
	byte[] secretKeyBytes = SECRET_KEY.getBytes();
	SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "HmacSHA256"); // Exemplo usando HMAC-SHA256


	private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora

	private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

	public String generateToken(String username, Map<String, Object> claims) {
		return Jwts.builder()
				.claims(claims)
				.subject(username)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(key)
				.compact();
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> resolver) {
		final Claims claims = extractAllClaims(token);
		return resolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		Jws<Claims> jws = Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token);
		return jws.getPayload();
	}

	public boolean validateToken(String token, String username) {
		return username.equals(extractUsername(token)) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}

	private static String generateRandomSecretKey(int keyLength) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			byte[] randomKey = new byte[keyLength];
			secureRandom.nextBytes(randomKey);
			return Base64.getEncoder().encodeToString(randomKey);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error generating random secret key", e);
		}
	}
}
