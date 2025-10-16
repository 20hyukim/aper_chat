package aper.aper_chat_renewal.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// TODO: 추후 프로덕션 환경에서는 제거 필요 - Aper-BE의 인증/인자를 사용해야 함.
@RestController
@RequestMapping("/test")
public class TestAuthController {
    
    @Value("${jwt.secret.key:your-secret-key-for-testing-minimum-32-characters}")
    private String secretKey;
    
    @Value("${jwt.expiration:360000000}") // around 4 days in milliseconds
    private long expiration;
    
    @PostMapping("/generate-token")
    public Map<String, String> generateTestToken(@RequestParam(defaultValue = "1") Long userId) {
        // Create a secret key from the string
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        
        // Generate token
        String token = Jwts.builder()
                .setSubject(userId.toString())
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("type", "Bearer");
        response.put("userId", userId.toString());
        response.put("expiresIn", String.valueOf(expiration / 1000) + " seconds");
        
        return response;
    }
    
    @GetMapping("/verify-token")
    public Map<String, Object> verifyToken(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("userId", claims.get("userId"));
            response.put("subject", claims.getSubject());
            response.put("issuedAt", claims.getIssuedAt());
            response.put("expiration", claims.getExpiration());
            
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("error", e.getMessage());
            return response;
        }
    }
}