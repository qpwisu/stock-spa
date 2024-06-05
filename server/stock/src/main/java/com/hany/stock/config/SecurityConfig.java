package com.hany.stock.config;

import com.hany.stock.auth.JwtTokenFilter;
import com.hany.stock.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private static String secretKey = "my-secret-key-123123";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()// security 기본 인증 비활성화
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 사용하지 않고 상태를 유지하지 않는 방식으로 설정하여, 모든 요청에 대해 매번 인증을 수행하도록 합니다.
                .and()
                //JWT 인증을 위한 커스텀 필터를 UsernamePasswordAuthenticationFilter 전에 추가, jwt 유효성 검사, 인증
                .addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests()
                .antMatchers("/boards/**/write").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/boards/**/modify").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/users/admin/**").hasAnyAuthority( "ADMIN")
                .anyRequest().permitAll()

                // 인증된 사용자만 접근 가능
//                .antMatchers("/jwt-login/info").authenticated()
                // admin 사용자만 접근 가능
//                .antMatchers("/jwt-login/admin/**").hasAuthority(UserRole.ADMIN.name())
                // 정의되지 않은 모든 요청은 인증 없이 접근을 허용합니다.
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/users/login"))
                .and()
                .logout() // 로그아웃 설정 추가
                .logoutUrl("/users/logout") // 로그아웃 처리 URL
                .logoutSuccessUrl("/") // 로그아웃 성공 후 리디렉션할 URL
                .deleteCookies("jwtToken") // 로그아웃 시 삭제할 쿠키 이름
                .invalidateHttpSession(true) // 세션 무효화
                // Security Config와는 달리 익명 클래스 사용
                .and().build();
    }
}
