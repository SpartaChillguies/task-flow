package min.taskflow.auth.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import min.taskflow.user.enums.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (StringUtils.hasText(token)) {

            try {
                Claims claims = jwtUtil.validateToken(token);
                Long userId = Long.valueOf(claims.getSubject());
                UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));
                // ✅ Authentication 객체 생성
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userId, // Principal (보통 User 객체, 여기선 userId)
                                null,   // Credentials
                                List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name())) // 권한
                        );

                // ✅ SecurityContext에 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // HttpServletRequest의 attribute에 저장
                request.setAttribute("userId", userId);
                request.setAttribute("userRole", userRole);

            } catch (Exception e) {
                log.error("JWT 토큰 검증 실패: ", e);
                // 토큰이 유효하지 않으면 attribute를 설정하지 않음
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 제거
        }
        return null;
    }
}