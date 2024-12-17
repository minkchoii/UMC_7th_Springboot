package spring.umc7th.domain.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum SocialType {
    KAKAO("kakao"),
    GOOGLE("google"),
    NAVER("naver"),
    APPLE("apple");

    private final String lowercase;

    SocialType(String lowercase) {
        this.lowercase = lowercase;
    }

    private static final Map<String, SocialType> BY_LOWERCASE =
            Stream.of(values()).collect(Collectors.toMap(SocialType::getLowercase, e -> e));

    public static SocialType valueOfLowercase(String lowercase) {
        return BY_LOWERCASE.get(lowercase);
    }
}
