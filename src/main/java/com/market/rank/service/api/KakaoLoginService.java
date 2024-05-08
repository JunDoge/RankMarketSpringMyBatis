package com.market.rank.service.api;

import com.market.rank.api.kakao.KakaoProfile;
import com.market.rank.api.kakao.KakaoTokenResponse;
import com.market.rank.config.api.KakaoLoginProperties;
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

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private final KakaoLoginProperties kakaoProperties;

    private final UsrViewService usrService;

    private final UsrCheckConfig usrCheck;


    public ReqLoginUsrDto getKakaUsrInfo(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");


        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.getClientId());
        params.add("redirect_uri", kakaoProperties.getRedirectUri());
        params.add("code", code);


        RestTemplate rt = new RestTemplate();
        KakaoTokenResponse tokenResponse = rt.postForObject(url, new HttpEntity<>(params, headers), KakaoTokenResponse.class);

        assert tokenResponse != null;
        return parsingUsrInfo(tokenResponse.getAccess_token(), headers);
    }

    private ReqLoginUsrDto parsingUsrInfo(String token, HttpHeaders headers) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String url = "https://kapi.kakao.com/v2/user/me";
        headers.add("Authorization", "Bearer " + token);


        RestTemplate rt = new RestTemplate();
        KakaoProfile resp = rt.postForObject(url, new HttpEntity<>(params,headers), KakaoProfile.class);

        String mail = Objects.requireNonNull(resp).getKakao_account().getEmail();

        ResUsrInfoDto usr  = usrService.searchUsrInfo(mail);
        if(usr == null){
            String usrNm = resp.getKakao_account().getProfile().getNickname();
            return usrCheck.usrIsEmpty(mail,usrNm);
        }else {
            return  usrCheck.usrNotEmpty(usr, token);
        }


    }

    public void unLink(String token){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        RestTemplate rt = new RestTemplate();
        rt.postForObject(url, new HttpEntity<>(params,headers), Long.class);

    }

}
