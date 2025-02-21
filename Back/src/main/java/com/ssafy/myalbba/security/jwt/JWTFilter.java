package com.ssafy.myalbba.security.jwt;

import com.ssafy.myalbba.Member.entity.UserEntity;
import com.ssafy.myalbba.security.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return super.shouldNotFilter(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    
        // ✅ 토큰 검사가 필요 없는 엔드포인트 목록
        String requestURI = request.getRequestURI();
        System.out.println(requestURI);
        if (requestURI.startsWith("/auth/signup") || requestURI.startsWith("/auth/login")
        || requestURI.startsWith("/auth/check/duplicate") || requestURI.startsWith("/auth/sendcode")
         || requestURI.startsWith("/auth/verifycode")) {
            System.out.println("토큰 검증 제외: " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }
    
        // Authorization 헤더 검증
        String authorization = request.getHeader("Authorization");
    
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            return;
        }
    
        System.out.println("authorization now");
        String token = authorization.split(" ")[1];
    
        // 토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);
            return;
        }
    
        // 토큰에서 userEmail과 role 획득
        String userEmail = jwtUtil.getUserEmail(token);
        String userName = jwtUtil.getUserName(token);
        String userRole = jwtUtil.getUserRole(token);
    
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userEmail);
        userEntity.setName(userName);
        userEntity.setPassword("temppassword");
        userEntity.setRole(userRole != null ? userRole : "ROLE_USER");
    
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    
        filterChain.doFilter(request, response);
    }

}