package com.market.rank.config.jwt;

import com.market.rank.dto.request.ReqLoginUsrDto;
import com.market.rank.service.usr.UsrProcService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TokenProvider {

    private final JwtProperties jwtProperties;

    private final UsrProcService usrProcService;

    private Key signatureAlgorith(){
        String base64EncodedSecretKey = Encoders.BASE64.encode(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }



    public String makeAccessToken(ReqLoginUsrDto usr, String role){
        Date now = new Date();
        Duration duration = Duration.ofDays(7);
        String pk = usr.getUsrId();

        if(usr.getUsrId() == null){
            pk = usr.getMail();
        }


        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + duration.toMillis()))
                .claim("id", pk)
                .claim("role", role)
                .claim("token", usr.getToken())
                .signWith(signatureAlgorith(), SignatureAlgorithm.HS256)
                .compact();
    }


    public void makeRefreshToken(ReqLoginUsrDto usr){
        Date now = new Date();
        Duration duration = Duration.ofHours(24);
        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + duration.toMillis()))
                .claim("id", usr.getUsrId())
                .claim("name", usr.getUsrNm())
                .signWith(signatureAlgorith(), SignatureAlgorithm.HS256)
                .compact();

        usrProcService.refreshTokenSave(refreshToken, usr);
    }

    public String getUsrId(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.get("id", String.class);

        }catch (CustomJwtExpiredException e){
            throw new CustomJwtExpiredException(e.getMessage());
        }
    }




    public boolean validToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(signatureAlgorith())
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public Authentication getAuthentication(String token){
        Claims claims = getClaims(token);
        String roles = claims.get("role", String.class);
        String usrId = claims.get("id", String.class);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(roles.split(","))
                        .map(role -> new SimpleGrantedAuthority(role.trim()))
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(
                new User(usrId,"" ,authorities),
                token,
                authorities);
    }


    private Claims getClaims(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(signatureAlgorith())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            throw new CustomJwtExpiredException(e.getMessage());

        }

    }

}
