//package min.taskflow.auth.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//

/*
JWT 인증 필터를 서블릿 필터로 등록하는 설정 클래스
 */
//@Configuration
//@RequiredArgsConstructor
//public class FilterConfig {
//
//    private final JwtUtil jwtUtil;
//
//    @Bean
//    public FilterRegistrationBean<JwtFilter> jwtFilter() {
//        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new JwtFilter(jwtUtil));
//        registrationBean.addUrlPatterns("/*");
//
//        return registrationBean;
//    }
//}
