package com.market.rank.controller.usr;

import com.market.rank.config.api.UsrCheckConfig;
import com.market.rank.dto.NaverSmsDto;
import com.market.rank.dto.request.*;
import com.market.rank.dto.response.ResDto;
import com.market.rank.dto.response.ResSinupDto;
import com.market.rank.service.api.NaverSmsService;
import com.market.rank.service.api.NurigoService;
import com.market.rank.service.usr.UsrProcService;
import com.market.rank.service.usr.UsrViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsrProcController {

    private final NaverSmsService naverSmsService;

    private final UsrViewService usrService;

    private final UsrProcService usrProcService;

    private final UsrCheckConfig usrCheckConfig;

    private final NurigoService nurigoService;


    @GetMapping("/address/chatroom/{chatId}")
    public ResponseEntity<ResDto> chatRoomAddr(@PathVariable("chatId") String chatId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResDto.builder()
                                .status(HttpStatus.OK)
                                .response(
                                        usrService.chatAddress(chatId)
                                )
                                .build()
                );
    }

    @PostMapping("/end/bid/product/{prdId}")
    public void bidEnd(@PathVariable("prdId") int prdId){
        usrProcService.bidEnd(prdId);
    }


    @PostMapping("/reload/accessToken")
    public ResponseEntity<ResDto> reloadAccessToken(){
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();
        ReqLoginUsrDto requestUsrDto = ReqLoginUsrDto.builder().usrId(usr_id).build();
        String refreshToken = usrService.searchRefreshToken(requestUsrDto);

        if(refreshToken != null){
            return usrCheckConfig.usrMakeToken(requestUsrDto);
        }

    return null;

    }


    @PostMapping("/validation/phone")
    public boolean validatePhone(NaverSmsDto naverSmsDto){
        System.out.println(naverSmsDto.getPh_num());
        if(usrService.usrCheck(naverSmsDto)){
            return false;
        }

        if (naverSmsService.sendSms(naverSmsDto) != null){
            usrProcService.phoneSave(naverSmsDto);
            return true;
        };

        return false;

    }


    @PostMapping("/validation/code")
    public boolean validateCode(NaverSmsDto naverSmsDto){
        return usrService.codeCheck(naverSmsDto);
    }


    @PostMapping("/signup")
    public ResponseEntity<ResDto> signup (ReqSignUpUsrDto reqSignUpUsrDto){

        ResSinupDto usr = usrProcService.usrSave(reqSignUpUsrDto);

        return usrCheckConfig.usrMakeToken(ReqLoginUsrDto.builder()
                .usrNm(usr.getUsr_name())
                .usrId(usr.getUsrId())
                .build());

    }
    @DeleteMapping("/delete/report/{rptId}")
    public void deleteReport(@PathVariable(value = "rptId") List<Integer> rpt_id){

        usrProcService.reportDelete(rpt_id);
    }
    @DeleteMapping("/delete/reviews")
    public void deleteReview(@RequestParam(value = "prdIds") List<Integer> prdIds){
        String usrId = SecurityContextHolder.getContext().getAuthentication().getName();
        usrProcService.reviewDelete(prdIds, usrId);
    }
    @PutMapping("/update/usr")
    public void usrUpdate(ReqUsrUpdateDto reqUsrUpdateDto) {
        String usrId = SecurityContextHolder.getContext().getAuthentication().getName();

        usrProcService.usrUpdate(reqUsrUpdateDto,usrId);
    }

    @DeleteMapping("/delete/bid/product")
    public void bidDel(@RequestParam(value = "prd_ids") List<Integer> prd_ids){
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();
        usrProcService.bidDel(prd_ids,usr_id);
    }

    @GetMapping("/update/wish")
    public void changeToWishList(@RequestParam(value = "prdId") List<Integer> prdIds){
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();
        usrProcService.changeToWishList(prdIds,usr_id);
    }

    @PostMapping("/add/review")
    public void addReview(ReqAddReview review){
        System.out.println(review.getRevDes());
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();
        usrProcService.reviewSave(review,usr_id);
    }



    @PostMapping("/add/report")
    public void addReport(ReqAddRptDto report){
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();
        usrProcService.rptSave(report, usr_id);

    }

    @PostMapping("/add/pay")
    public void payment(ReqPayDto pay) {
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();
        usrProcService.paySave(pay, usr_id);
        String ph_num = usrService.usrPhNum(usr_id);
        nurigoService.sendOne(pay, ph_num);
    }

    @PostMapping("/add/dealDtm")
    public void delDtm(ReqAddDelDtm reqAddDelDtm ) {
        usrProcService.delDtmSave(reqAddDelDtm);
    }

}