package com.market.rank.controller.usr;

import com.market.rank.config.api.UsrCheckConfig;
import com.market.rank.dto.request.ReqLoginUsrDto;
import com.market.rank.dto.response.ResDto;
import com.market.rank.dto.response.ResUsrInfoDto;
import com.market.rank.service.api.GoogleLoginService;
import com.market.rank.service.api.KakaoLoginService;
import com.market.rank.service.api.NaverLoginService;
import com.market.rank.service.usr.UsrProcService;
import com.market.rank.service.usr.UsrViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class

OauthUsrController {

    private final KakaoLoginService kakaoLoginService;

    private final NaverLoginService naverLoginService;

    private final GoogleLoginService googleLoginService;

    private final UsrProcService usrProcService;

    private final UsrViewService usrViewService;

    private final UsrCheckConfig usrCheck;


    @GetMapping("/naver/{code}")
    public  ResponseEntity<ResDto> naverLogin(@PathVariable("code") String code){
        System.out.println('+' +code);

        ReqLoginUsrDto usr = naverLoginService.getNaverUsrInfo(code);

        return usrCheck.usrMakeToken(usr);

    }

    @GetMapping("/kakao/{code}")
    public ResponseEntity<ResDto> kakaoLogin(@PathVariable("code") String code){
        ReqLoginUsrDto usr = kakaoLoginService.getKakaUsrInfo(code);
        return usrCheck.usrMakeToken(usr);

    }

    @DeleteMapping("/delete/usr")
    public void usrDelete(@RequestHeader(value="Authorization") String accessToken) {
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();
        usrProcService.usrDelete(usr_id);
    }

    @GetMapping("/google/{code}")
    public  ResponseEntity<ResDto> googleLogin(@PathVariable("code") String code){
        ReqLoginUsrDto usr = googleLoginService.getGoogleUsrInfo(code);
        return usrCheck.usrMakeToken(usr);
    }



    @PostMapping("/login/mobile")
    public ResponseEntity<ResDto> mobileLogin(@RequestBody Map<String, String> payload) {

        System.out.println(payload.get("mail"));
        ResUsrInfoDto usr = usrViewService.searchUsrInfo(payload.get("mail"));
        ReqLoginUsrDto reqLoginUsrDto;
        if(usr != null){

            usrProcService.deviceTokenSave(payload.get("token"), usr.getUsrId());
            reqLoginUsrDto = ReqLoginUsrDto
                    .builder()
                    .usrId(usr.getUsrId())
                    .usrNm(usr.getUsrNm())
                    .build();
        }else{
            reqLoginUsrDto = ReqLoginUsrDto
                    .builder()
                    .mail(payload.get("mail"))
                    .usrNm("")
                    .build();
        }

        return usrCheck.usrMakeToken(reqLoginUsrDto);

    }

}
