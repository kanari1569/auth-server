package com.mpt.authservice.SocialLogin;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mpt.authservice.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginTokenManager {
    @Value("${Token.secret-key}")
    private String SECRET_KEY;
    
    public String genToken(User user){
        return Jwts.builder()
            .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
            .setSubject("Login")
            .setClaims(user.getHashMap())
            .setIssuer("Musinsa-Price-Tracker")
            .setIssuedAt(new Date())
            .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
            .compact();
    }

    public boolean isValid(String token) {
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
            
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String refreshToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody();

        return genToken(User.builder()
                            .userID(claims.get("userID",String.class))
                            .email(claims.get("email",String.class))
                            .platform(claims.get("platform",String.class))
                            .build());
    }

    public String refreshToken(Claims claims) {
        return genToken(User.builder()
                            .userID(claims.get("userID",String.class))
                            .email(claims.get("email",String.class))
                            .platform(claims.get("platform",String.class))
                            .build());
    }

    public Claims getClaims(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody();
        
        return claims;
    }
}
