package com.hany.stock.auth;

import com.hany.stock.domain.entity.User;
import com.hany.stock.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// OncePerRequestFilter : 매번 들어갈 때 마다 체크 해주는 필터
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            // Header의 Authorization 값이 비어있으면 => Jwt Token을 전송하지 않음
            if(authorizationHeader == null) {

                // 화면 로그인 시 쿠키의 "jwtToken"로 Jwt Token을 전송
                // 쿠키에도 Jwt Token이 없다면 로그인 하지 않은 것으로 간주
                if(request.getCookies() == null) {
                    filterChain.doFilter(request, response);
                    return;
                }

                // 쿠키에서 "jwtToken"을 Key로 가진 쿠키를 찾아서 가져오고 없으면 null return
                Cookie jwtTokenCookie = Arrays.stream(request.getCookies())
                        .filter(cookie -> cookie.getName().equals("jwtToken"))
                        .findFirst()
                        .orElse(null);

                if(jwtTokenCookie == null) {
                    filterChain.doFilter(request, response);
                    return;
                }

                // 쿠키 Jwt Token이 있다면 이 토큰으로 인증, 인가 진행
                String jwtToken = jwtTokenCookie.getValue();
                authorizationHeader = "Bearer " + jwtToken;
            }

            // Header의 Authorization 값이 'Bearer '로 시작하지 않으면 => 잘못된 토큰
            if(!authorizationHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // 전송받은 값에서 'Bearer ' 뒷부분(Jwt Token) 추출
            String token = authorizationHeader.split(" ")[1];

            // 전송받은 Jwt Token이 만료되었으면 => 다음 필터 진행(인증 X)
            if(JwtTokenUtil.isExpired(token, secretKey)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Jwt Token에서 loginId 추출
            String loginId = JwtTokenUtil.getLoginId(token, secretKey);

            // 추출한 loginId로 User 찾아오기
            User loginUser = userService.getLoginUserByLoginId(loginId);

            if ( loginUser == null) {
                throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
            }

            // loginUser 정보로 UsernamePasswordAuthenticationToken 발급
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginUser.getLoginId(), null, List.of(new SimpleGrantedAuthority(loginUser.getUserRole().name())));
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 권한 부여
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            // 인증 실패 핸들러 호출 쿠키는 있는데 id가 삭제된경우 쿠기 제거
            deleteJwtTokenCookie(request, response);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/users/login?error=true");        }
    }
    private void deleteJwtTokenCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null); // 쿠키 이름과 null 값을 가진 쿠키 생성
        cookie.setPath("/"); // 경로 설정
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 쿠키의 유효기간을 0으로 설정하여 즉시 삭제하도록 함
        response.addCookie(cookie); // 응답에 쿠키 추가하여 클라이언트에 전송, 쿠키 삭제
    }
}

