package com.project.msy.auth.oauth;

import com.project.msy.user.entity.Provider;
import com.project.msy.user.entity.Role;
import com.project.msy.user.entity.User;
import com.project.msy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google, kakao
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oauth2User.getAttributes();
        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes);

        User user = saveOrUpdate(oAuthAttributes, registrationId);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuthAttributes.getAttributes(),
                oAuthAttributes.getNameAttributeKey()
        );
    }

    private User saveOrUpdate(OAuthAttributes attributes, String providerName) {
        String providerId = attributes.getProviderId();
        String oauthId   = providerName.toUpperCase() + "_" + providerId;
        Provider provider = Provider.valueOf(providerName.toUpperCase());

        // 1) oauthId + provider 로 먼저 조회
        Optional<User> userOpt = userRepository.findByOauthIdAndProvider(oauthId, provider);

        // 2) 이메일이 제공된 경우, 기존 이메일 중복 계정 조회
        if (!userOpt.isPresent() && attributes.getEmail() != null) {
            userOpt = userRepository.findByEmail(attributes.getEmail());
        }

        // 3) 기존 사용자 있으면 반환, 없으면 새 사용자 등록
        if (userOpt.isPresent()) {
            return userOpt.get();
        } else {
            return saveNewUser(attributes, provider, oauthId);
        }
    }

    private User saveNewUser(OAuthAttributes attributes, Provider provider, String oauthId) {
        User newUser = User.builder()
                .oauthId(oauthId)
                .name(attributes.getName())
                .email(attributes.getEmail())   // 카카오는 null일 수 있음
                .provider(provider)
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
        return userRepository.save(newUser);
    }
}
