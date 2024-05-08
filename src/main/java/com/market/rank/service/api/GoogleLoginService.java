package com.market.rank.service.api;

import com.market.rank.api.google.GoogleProfile;
import com.market.rank.api.google.GooleTokenResponse;
import com.market.rank.config.api.GoogleLoginProperties;
import com.market.rank.config.api.UsrCheckConfig;
import com.market.rank.dto.request.ReqLoginUsrDto;
import com.market.rank.dto.response.ResUsrInfoDto;
import com.market.rank.service.usr.UsrViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GoogleLoginService {

    private final GoogleLoginProperties googleProperties;

    private final UsrViewService usrService;

    private final UsrCheckConfig usrCheck;

    public ReqLoginUsrDto getGoogleUsrInfo(String code){
        String url = "https://oauth2.googleapis.com/token";
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                params.add("code", code);
                params.add("grant_type", "authorization_code");
                params.add("client_id", googleProperties.getClientId());
                params.add("client_secret", googleProperties.getClientSecret());
                params.add("redirect_uri", googleProperties.getRedirectUri());



        RestTemplate rt = new RestTemplate();
        GooleTokenResponse resp = rt.postForObject(url, new HttpEntity<>(params, headers), GooleTokenResponse.class);
        System.out.println(Objects.requireNonNull(resp).getAccess_token());

        return pasingUsrInfo(resp.getAccess_token());
    }

    private ReqLoginUsrDto pasingUsrInfo(String token){
        String url = "https://www.googleapis.com/userinfo/v2/me";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();


        RestTemplate rt = new RestTemplate();
        GoogleProfile resp = rt.exchange(url, HttpMethod.GET, new HttpEntity<>(params,headers), GoogleProfile.class).getBody();
        String mail = Objects.requireNonNull(resp).getEmail();
        String usrNm = resp.getName();
        ResUsrInfoDto usr  = usrService.searchUsrInfo(mail);
        if(usr == null){
            return usrCheck.usrIsEmpty(mail,usrNm);
        }else {
            return  usrCheck.usrNotEmpty(usr, token);
        }

    }
}
