package min.taskflow.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import min.taskflow.common.annotation.Auth;
import min.taskflow.common.dto.AuthUser;
import min.taskflow.user.enums.UserRole;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 컨트롤러 메서드 파라미터에서 @Auth 어노테이션이 붙은 AuthUser 객체를
 * 자동으로 주입해주는 ArgumentResolver.
 */
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    //해당 파라미터가 @Auth 어노테이션과 AuthUser 타입을 동시에 가지고 있는지 판단
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean haveAuth = parameter.getParameterAnnotation(Auth.class) != null;
        boolean isValidType = parameter.getParameterType().equals(AuthUser.class);

        if (haveAuth != isValidType) {
            throw new IllegalArgumentException("Auth 와 AuthUser 가 함께 사용되어야 합니다.");
        }

        return haveAuth;
    }

    //실제 파라미터에 주입할 객체를 생성하는 메서드
    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();

        Long userId = (Long) httpServletRequest.getAttribute("userId");
        UserRole userRole = (UserRole) httpServletRequest.getAttribute("userRole");

        if (userId != null && userRole != null) {
            return new AuthUser(userId, userRole);
        }

        return null;
    }
}