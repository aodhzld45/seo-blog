package springbootdeveloper.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import springbootdeveloper.service.UserDetailService;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserDetailService userService;

//    스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer confugure() {
        return (web) -> web.ignoring() // web.ignoring() 필터 적용 제외
                .requestMatchers(toH2Console()) // http://localhost:8080/h2-console 접속 허용
                .requestMatchers("/static/**"); // 정적 파일 html, css, js등 Resources
    }

//  특정 HTTP 요청에 대한 웹기반 보안 구성.
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return http
                    .authorizeRequests() // 인증, 인가 설정
                    .requestMatchers("/login", "/signup", "/user").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin() // form 기반 로그인 설정
                    .loginPage("/login")
                    .defaultSuccessUrl("/articles")
                    .and()
                    .logout() // logout 설정
                    .logoutSuccessUrl("/login")
                    .invalidateHttpSession(true)
                    .and()
                    .csrf().disable() // csrf 비활성화 -> 보안 취약점 관련.
                    .build();
}

// 인증 관리자 관련 설정.
@Bean
public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userService) // 사용자 정보 서비스 설정
            .passwordEncoder(bCryptPasswordEncoder)
            .and()
            .build();
}

    @Bean // 패스워드 Encoder로 사용할 빈 등록.
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
