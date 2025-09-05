package min.taskflow.auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import min.taskflow.user.enums.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/*
토큰을 생성하고 검증하는 유틸 클래스
 */
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private final long EXPIRATION = 1000L * 60 * 60; // 1시간
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        // HS256 → 최소 32바이트 이상의 key 필요
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    //토큰 생성
    public String createToken(Long userId, UserRole userRole) {

        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("userRole", userRole)
                        .setExpiration(new Date(date.getTime() + EXPIRATION))
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // JWT 검증
    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
