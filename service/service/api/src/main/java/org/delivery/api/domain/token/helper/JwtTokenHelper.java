package org.delivery.api.domain.token.helper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.delivery.common.error.ErrorCode;
import org.delivery.common.error.TokenErrorCode;
import org.delivery.common.exception.ApiException;
import org.delivery.api.domain.token.ifs.TokenHelperIfs;
import org.delivery.api.domain.token.model.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenHelper implements TokenHelperIfs {

    @Value("${token.secret.key}")
    private String secretKey;

    @Value("${token.access-token.plus-hour}")
    private Long accessTokenPlusHour;

    @Value("${token.refresh-token.plus-hour}")
    private Long refreshTokenHour;

    @Override
    public TokenDto issueAccessToken(Map<String, Object> data) { // data -> userId
        
        // 만료시간 정하기
        var expiredAtLocalDateTime = LocalDateTime.now().plusHours(accessTokenPlusHour);

        // 토큰의 만료시간 구하기
        var expiredAt = Date.from(
                expiredAtLocalDateTime
                        .atZone(
                                ZoneId.systemDefault()
                        ).toInstant()
        );

        // 서명키 만들기
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        // 토큰 만들기
        var jwtToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setClaims(data)
                .setExpiration(expiredAt)
                .compact();

        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredAtLocalDateTime)
                .build();
    }

    @Override
    public TokenDto issueRefreshToken(Map<String, Object> data) {

        // 만료시간 정하기
        var expiredAtLocalDateTime = LocalDateTime.now().plusHours(refreshTokenHour);

        // 토큰의 만료시간 구하기
        var expiredAt = Date.from(
                expiredAtLocalDateTime
                        .atZone(
                                ZoneId.systemDefault()
                        ).toInstant()
        );

        // 서명키 만들기
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        // 토큰 만들기
        var jwtToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setClaims(data)
                .setExpiration(expiredAt)
                .compact();

        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredAtLocalDateTime)
                .build();
    }

    @Override
    public Map<String, Object> validationTokenWithThrow(String token) {

        // 키 만들기
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        var parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        try {
            var result = parser.parseClaimsJws(token);
            return new HashMap<String,Object>(result.getBody());

        } catch(Exception e) {
            if(e instanceof SignatureException) {
                // 토큰이 유효하지 않을떄
                throw new ApiException(TokenErrorCode.INVALID_TOKEN,e);
            } else if(e instanceof ExpiredJwtException) {
                // 만료된 토큰일 때
                throw new ApiException(TokenErrorCode.EXPIRED_TOKEN,e);
            } else {
                // 그외 에러
                throw new ApiException(TokenErrorCode.TOKEN_EXCEPTION,e);
            }


        }
    }
}
