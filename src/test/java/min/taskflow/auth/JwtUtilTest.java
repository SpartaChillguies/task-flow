package min.taskflow.auth;

import io.jsonwebtoken.Claims;
import min.taskflow.auth.config.JwtUtil;
import min.taskflow.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtUtilTest {
    private final String secretKey = "mySecretKeyForTestingPurposesOnly1234567890";
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secretKey", secretKey);
        jwtUtil.init();
    }

    @Test
    void createToken_토큰생성시_생성성공() {
        // given
        Long userId = 1L;
        UserRole userRole = UserRole.USER;

        // when
        String token = jwtUtil.createToken(userId, userRole);

        // then
        assertThat(token).isNotNull();
        assertThat(token).startsWith("Bearer ");
    }

    @Test
    void validateToken_토큰검증_검증성공() {
        // given
        Long userId = 1L;
        UserRole userRole = UserRole.USER;
        String token = jwtUtil.createToken(userId, userRole);
        String jwtToken = token.substring(7); // "Bearer " 제거

        // when
        Claims claims = jwtUtil.validateToken(jwtToken);

        // then
        assertThat(claims.getSubject()).isEqualTo(String.valueOf(userId));
        assertThat(claims.get("userRole", String.class)).isEqualTo(userRole.name());
    }
}
