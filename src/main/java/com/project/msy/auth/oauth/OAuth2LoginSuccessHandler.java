package com.project.msy.auth.oauth;

import com.project.msy.config.JwtUtil;
import com.project.msy.user.entity.Provider;
import com.project.msy.user.entity.User;
import com.project.msy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * OAuth2 로그인 성공 시 호출되는 핸들러
 * – Postman 등 “프론트 없는” 테스트 시에는 JSON 응답 사용
 * – 실제 프론트 연동 시에는 redirect URL 로 토큰 전달
 */
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // 1) 인증된 OAuth2User 가져오기
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = authToken.getAuthorizedClientRegistrationId(); // "google" or "kakao"

        // 2) providerId 추출 및 User 조회
        String providerId = oauthUser.getName(); // e.g. 구글 sub, 카카오 id
        Provider provider = Provider.valueOf(registrationId.toUpperCase());
        User user = userRepository.findByOauthIdAndProvider(registrationId.toUpperCase() + "_" + providerId, provider)
                .orElseThrow(() -> new IllegalStateException("OAuth 사용자 조회 실패"));

        // 3) JWT 생성 (subject: 일반회원은 userId, 소셜회원은 oauthId)
        String subject = (user.getUserId() != null) ? user.getUserId() : user.getOauthId();
        String token   = jwtUtil.generateToken(subject, user.getRole().name());

        // =========================
        // ✅ 4-A: JSON 응답 (Postman 테스트용)
        // =========================

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + token + "\"}");


        // =========================
        // ✅ 4-B: Redirect 응답 (프론트 연동용)
        // =========================
        // String frontBaseUrl = "http://localhost:3000/oauth2/success";
        // String redirectUrl = frontBaseUrl
        //     + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
        // response.sendRedirect(redirectUrl);
    }
}
