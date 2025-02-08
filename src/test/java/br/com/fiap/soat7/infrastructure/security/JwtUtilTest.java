package br.com.fiap.soat7.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    private String validToken;
    private String expiredToken;
    private String invalidSignatureToken;
    private String username = "test@example.com";

    private String testSecretKey = "testSecretKey1234567890123456789012"; // Must be long enough for HMAC-SHA256

    @BeforeEach
    public void setup() {
        jwtUtil = new JwtUtil();

        // Override the key-related fields, but not the static SECRET_KEY
        Key key = Keys.hmacShaKeyFor(testSecretKey.getBytes());
        SecretKey secretKey = new SecretKeySpec(testSecretKey.getBytes(), "HmacSHA256");

        ReflectionTestUtils.setField(jwtUtil, "key", key);
        ReflectionTestUtils.setField(jwtUtil, "secretKey", secretKey);

        Map<String, Object> claims = new HashMap<>();
        validToken = jwtUtil.generateToken(username, claims);

        // Generate an expired token
        long expirationTime = System.currentTimeMillis() - 1000; // Expired 1 second ago
        expiredToken = Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis() - 2000))  // Issued 2 seconds ago
                .expiration(new Date(expirationTime))
                .signWith(Keys.hmacShaKeyFor(testSecretKey.getBytes()))
                .compact();


        //Generate a token with an invalid Signature
        String wrongSecretKey = "wrongSecretKey1234567890123456789012";
        invalidSignatureToken = Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(Keys.hmacShaKeyFor(wrongSecretKey.getBytes()))
                .compact();

    }

    @Test
    public void whenGenerateToken_thenReturnValidToken() {
        assertThat(validToken).isNotBlank();
    }

    @Test
    public void whenExtractUsername_thenReturnUsername() {
        String extractedUsername = jwtUtil.extractUsername(validToken);
        assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    public void whenExtractClaim_thenReturnClaimValue() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("testClaim", "testValue");
        String tokenWithClaim = jwtUtil.generateToken(username, claims);

        String extractedClaim = jwtUtil.extractClaim(tokenWithClaim, c -> (String) c.get("testClaim"));

        assertThat(extractedClaim).isEqualTo("testValue");
    }

    @Test
    public void whenValidateToken_withValidToken_thenReturnTrue() {
        boolean isValid = jwtUtil.validateToken(validToken, username);
        assertTrue(isValid);
    }

    @Test
    public void whenValidateToken_withInvalidUsername_thenReturnFalse() {
        boolean isValid = jwtUtil.validateToken(validToken, "wrong@example.com");
        assertFalse(isValid);
    }


    @Test
    public void whenIsTokenExpired_withValidToken_thenReturnFalse() {
        // Use ReflectionTestUtils to call private method
        boolean isExpired = (boolean) ReflectionTestUtils.invokeMethod(jwtUtil, "isTokenExpired", validToken);
        assertFalse(isExpired);
    }

    @Test
    public void whenExtractClaim_withInvalidToken_thenThrowsException() {
        String tamperedToken = validToken.substring(0, validToken.length() - 5) + "xxxxx"; // Tamper the token
        assertThrows(SignatureException.class, () -> jwtUtil.extractClaim(tamperedToken, Claims::getSubject));
    }

    @Test
    public void whenExtractUsername_withInvalidToken_thenThrowsException() {
        String tamperedToken = validToken.substring(0, validToken.length() - 5) + "xxxxx"; // Tamper the token
        assertThrows(SignatureException.class, () -> jwtUtil.extractUsername(tamperedToken));
    }

}