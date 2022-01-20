package com.example.todoapp.security;

import com.example.todoapp.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "testjujuwon";

    public String create(UserEntity userEntity) {
        // 기한은 지금부터 1일로 설정
        Date expiryDate = Date.from(
            Instant.now()
            .plus(1, ChronoUnit.DAYS));

        // JWT Token 생성
        return Jwts.builder()
            // header 에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            // payload 에 들어갈 내용
            .setSubject(userEntity.getId()) // sub
            .setIssuer("todo app") // iss
            .setIssuedAt(new Date()) // iat
            .setExpiration(expiryDate) //exp
            .compact();
    }

    public String validateAndGetUserId(String token) {
        // parseClaimsJws 메소드가 Base64 로 디코딩 및 파싱
        // 헤더와 페이로드를 setSigningKey 로 넘어온 시크릿을 이용해 서명한 후 token 의 서명과 비교
        // 위조되지 않았다면 페이로드(Claims) 리턴, 위조라면 예외
        // 그 중 여기서는 userId 가 필요하므로 getBody 호출
        Claims claims = Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }
}
