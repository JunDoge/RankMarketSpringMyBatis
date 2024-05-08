package com.market.rank.config.api;

import com.market.rank.config.jwt.TokenProvider;
import com.market.rank.dto.request.ReqLoginUsrDto;
import com.market.rank.dto.response.ResDto;
import com.market.rank.dto.response.ResUsrDto;
import com.market.rank.dto.response.ResUsrInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsrCheckConfig {


    private final TokenProvider tokenProvider;

    public ReqLoginUsrDto usrIsEmpty(String email, String usrNm){
        return ReqLoginUsrDto.builder()
                .mail(email)
                .usrNm(usrNm)
                .build();

    }

    public ReqLoginUsrDto usrNotEmpty(ResUsrInfoDto usr, String token){

        return ReqLoginUsrDto.builder()
                .usrId(usr.getUsrId())
                .usrNm(usr.getUsrNm())
                .token(token)
                .build();
    }

    public ResponseEntity<ResDto> usrMakeToken(ReqLoginUsrDto usr){
        String token;
        if(usr.getUsrId() == null) {
            System.out.println("권한1번");
            token = tokenProvider.makeAccessToken(usr, "ROLE_GUEST");
        }else{
            System.out.println("권한2번");
            tokenProvider.makeRefreshToken(usr);
            token = tokenProvider.makeAccessToken(usr,"ROLE_USER");

        }

        System.out.println(token);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(ResDto.builder()
                        .status(HttpStatus.OK)
                        .response(
                                ResUsrDto.builder()
                                        .mail(usr.getMail())
                                        .usrNm(usr.getUsrNm())
                                        .build())
                        .build());

    }
}
