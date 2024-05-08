package com.market.rank.service.api;

import com.market.rank.api.naver.NaverProfile;
import com.market.rank.api.naver.NaverTokenResponse;
import com.market.rank.config.api.NaverLoginProperties;
import com.market.rank.config.api.UsrCheckConfig;
import com.market.rank.dto.request.ReqLoginUsrDto;
import com.market.rank.dto.response.ResUsrInfoDto;
import com.market.rank.service.usr.UsrViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NaverLoginService {

    private final NaverLoginProperties naverProperties;

    private final UsrViewService usrService;

    private final UsrCheckConfig usrCheck;

    public ReqLoginUsrDto getNaverUsrInfo(String code) {

        String url = "https://nid.naver.com/oauth2.0/token";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", naverProperties.getClientId());
        params.add("client_secret", naverProperties.getClientSecret());
        params.add("redirect_uri", naverProperties.getRedirectUri());
        params.add("code", code);
        HttpHeaders headers = new HttpHeaders();

        RestTemplate rt = new RestTemplate();
        NaverTokenResponse resp = rt.postForObject(url, new HttpEntity<>(params,headers), NaverTokenResponse.class);
        System.out.println("ddddddd" + resp.getAccess_token());

        return getUsrInfo(resp.getAccess_token());

    }


    private ReqLoginUsrDto getUsrInfo(String token) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String url = "https://openapi.naver.com/v1/nid/me";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);


        RestTemplate rt = new RestTemplate();
        NaverProfile resp = rt.postForObject(url,  new HttpEntity<>(params, headers), NaverProfile.class);

        String mail = resp.getResponse().getEmail();


        ResUsrInfoDto usr  = usrService.searchUsrInfo(mail);
        if(usr == null){
            String usrNm = resp.getResponse().getNickname();
            return usrCheck.usrIsEmpty(mail,usrNm);
        }else {
            return  usrCheck.usrNotEmpty(usr, token);
        }

    }
}
