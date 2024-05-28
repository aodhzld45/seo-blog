package springbootdeveloper.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    private SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 시크릿 키를 생성

    //토큰 생성 메서드
    public String create(String email){
        Date expireDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS)); // 토큰 만료시각

        String jwt = Jwts.builder()
                .signWith(key)
                .setSubject(email)
                .setIssuedAt(new Date()).setExpiration(expireDate)
                .compact();

        return jwt;
    }

    public String validate(String jwt) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt);
            return claims.getBody().getSubject();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


}
