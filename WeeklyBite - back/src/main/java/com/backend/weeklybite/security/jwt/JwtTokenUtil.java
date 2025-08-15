package com.backend.weeklybite.security.jwt;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    @Value("${jwt.secret}")
    private String secret;

    private final long EXPIRATION_TIME = 1000 * 5 * 60 * 60;
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // Generate a JWT token with roles
    public String generateToken(String username, List<String> roles) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withSubject(username)
                .withClaim("role", roles) // Dodavanje uloga kao claim
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    // Get username from JWT token
    public String extractUsername(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        return decodedJWT.getSubject();
    }

    // Get roles from JWT token
    public List<String> extractRoles(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        return decodedJWT.getClaim("role").asList(String.class); // Dohvata uloge iz claim-a
    }

    // Validate if the token is expired
    public boolean isTokenExpired(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        System.out.println("Token issued at: " + decodedJWT.getIssuedAt());
        System.out.println("Token expires at: " + decodedJWT.getExpiresAt());
        return decodedJWT.getExpiresAt().before(new Date());
    }

    private DecodedJWT decodeToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            DecodedJWT jwt = JWT.require(algorithm)
                    .build()
                    .verify(token);
            return jwt;
        } catch (JWTVerificationException exception) {
            // Log error
            System.out.println("Error verifying token: " + exception.getMessage());
            throw exception;  // Or handle accordingly
        }
    }

    // Validate the token
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
