package spring.umc7th.config.security;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import spring.umc7th.domain.Member;
import spring.umc7th.domain.enums.Gender;
import spring.umc7th.domain.enums.Role;
import spring.umc7th.domain.enums.SocialType;
import spring.umc7th.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = "null";
        String nickname = "null";

        if (provider.equals("kakao")) {
            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

            nickname = (String) properties.get("nickname");
            email = nickname + "@kakao.com"; // 임시 이메일 생성
        } else if (provider.equals("google")) {
            nickname = (String) attributes.get("name");
            email = nickname + "@google.com"; // 임시 이메일 생성
        } else if (provider.equals("naver")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");

            nickname = (String) response.get("nickname");
            email = nickname + "@naver.com"; // 임시 이메일 생성
        }

        // 사용자 정보 저장 또는 업데이트
        Member member = saveOrUpdateUser(email, nickname, provider);

        // 이메일을 Principal로 사용하기 위해 attributes 수정
        Map<String, Object> modifiedAttributes = new HashMap<>(attributes);
        modifiedAttributes.put("email", email);

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                modifiedAttributes,
                "email"  // email Principal로 설정
        );
    }

    private Member saveOrUpdateUser(String email, String nickname, String provider) {
        Member member = memberRepository.findByEmail(email)
                .orElse(Member.builder()
                        .email(email)
                        .name(nickname)
                        .password(passwordEncoder.encode("OAUTH_USER_" + UUID.randomUUID()))
                        .gender(Gender.NONE)  // 기본값 설정
                        .address("소셜로그인")  // 기본값 설정
                        .specAddress("소셜로그인")  // 기본값 설정
                        .socialType(SocialType.valueOfLowercase(provider))
                        .role(Role.USER)
                        .build());

        return memberRepository.save(member);
    }
}
