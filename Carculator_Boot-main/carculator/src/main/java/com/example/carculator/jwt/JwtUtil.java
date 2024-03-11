package com.example.carculator.jwt;

import com.example.carculator.auth.PrincipalDetailsService;
import com.example.carculator.redis.entity.RedisRefreshToken;
import com.example.carculator.redis.repository.RedisRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final PrincipalDetailsService principalDetailsService;

    private final RedisRepository redisRepository;


    private static final long ACCESS_TIME = 24 * 60 * 60 * 1000L; // 1일 24 * 60 * 60 * 1000L;
    public static final long REFRESH_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일
    public static final String ACCESS_TOKEN = "Access";
    public static final String REFRESH_TOKEN = "Refresh";
    public static String BEARER_PREFIX = "Bearer ";


    @Value("${jwt.secretKey}")
    private String secretKey;

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // header 토큰을 가져오는 기능
    public String getHeaderToken(HttpServletRequest request, String type) {
        String bearerToken = type.equals("Access") ? request.getHeader(ACCESS_TOKEN) : request.getHeader(REFRESH_TOKEN);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {

            log.info(bearerToken);

            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 생성
    public TokenDto createAllToken(String email) {
        return new TokenDto(createToken(email, "Access"), createToken(email, "Refresh"));
    }

    public String createToken(String email, String type) {

        Date date = new Date();

        long time = type.equals("Access") ? ACCESS_TIME : REFRESH_TIME;

        Claims claims = Jwts.claims()
                .setSubject(email)
                .setIssuedAt(date) //생성일 설정
                .setExpiration(new Date(date.getTime() + time)); //만료일 설정


        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
//                .setSubject(email)
//                .setExpiration(new Date(date.getTime() + time))
//                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();

    }

    // 토큰 검증
    public Boolean tokenValidation(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    public Integer validateAccessToken(String accessToken) {
        try {

            log.info("accessToken 검증");

            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);  //parseClaimsJws 들어가면 예외 볼수있음

            log.info("accessToken 1");

            return 1;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("0" + e.getMessage());
            return 0;
        } catch (ExpiredJwtException e) {
            log.info("2" + e.getMessage());

            return 2;
        } catch (UnsupportedJwtException e) {
            log.info("3" + e.getMessage());
            return 0;
        } catch (IllegalArgumentException e) {
            log.info("4" + e.getMessage());
            return 0;
        }
    }

    // refreshToken 토큰 검증
    public Boolean refreshTokenValidation(String token) {
        // 1차 토큰 검증
        if (!tokenValidation(token)) return false;

        // DB에 저장한 토큰 비교
        Optional<RedisRefreshToken> refreshToken = redisRepository.findById(getEmailFromToken(token));

        return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken().substring(7));
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String email) {
        UserDetails userDetails = principalDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 email 가져오는 기능
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}