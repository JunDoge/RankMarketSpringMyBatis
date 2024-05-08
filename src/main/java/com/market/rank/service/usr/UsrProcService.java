package com.market.rank.service.usr;

import com.market.rank.dto.NaverSmsDto;
import com.market.rank.dto.request.*;
import com.market.rank.dto.response.ResChatMessagesDto;
import com.market.rank.dto.response.ResChatUsersDto;
import com.market.rank.dto.response.ResPushDto;
import com.market.rank.dto.response.ResSinupDto;
import com.market.rank.mapper.usr.UsrProcMapper;
import com.market.rank.service.api.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UsrProcService {



    private final FcmService fcmService;

    private final RedisTemplate<String, String> redisTemplate;

    private final UsrProcMapper usrProcMapper;

    public ResSinupDto usrSave(ReqSignUpUsrDto reqSignUpUsrDto) {
        usrProcMapper.usrSave(reqSignUpUsrDto);
        return usrProcMapper.usrLastSelect(reqSignUpUsrDto.getMail());
    }

    public void phoneSave(NaverSmsDto naverSmsDto) {
        redisTemplate.opsForValue().set(naverSmsDto.getPh_num(), naverSmsDto.getPh_code(), 3, TimeUnit.MINUTES);
    }

    public void refreshTokenSave(String refreshToken, ReqLoginUsrDto usr) {
        redisTemplate.opsForValue().set(usr.getUsrId(), refreshToken, 24, TimeUnit.HOURS);
    }


    public void reviewSave(ReqAddReview review, String usr_id) {
        usrProcMapper.reviewSave(review,usr_id);
    }

    public void bidEnd(int prd_id) {
        System.out.println("여기");
        try {
            usrProcMapper.winSave(prd_id);
            System.out.println("여기까진 진짜 실행이 된다" + prd_id);

            ResPushDto buyerInfo = usrProcMapper.pushBuyerInfo(prd_id);



            String sellerToken = usrProcMapper.pushSellerInfo(prd_id);
            if (buyerInfo.getToken() != null) {
                System.out.println("여기는 진짜 실행됨? bid부분임    " + buyerInfo.getTitle());
                fcmService.send_FCM(buyerInfo.getToken(), "랭크마켓", buyerInfo.getTitle() + " 상품이 낙찰되었습니다.");
            }
            System.out.println("여기 sellUsr   " + sellerToken);
            if (sellerToken != null) {
                fcmService.send_FCM(sellerToken, "랭크마켓",  buyerInfo.getTitle() + " 상품이 낙찰되었습니다.");
            }
        } catch (Exception ignored) {
            System.out.println(ignored);
        }

        usrProcMapper.prdBidCntUpdate(prd_id);

    }


    public void rptSave(ReqAddRptDto report, String usr_id) {
       usrProcMapper.rptSave(report, usr_id);
    }

    public void paySave(ReqPayDto pay, String usr_id) {

        for (int i = 0; i < pay.getPrdIds().size(); i++) {

            System.out.println("prd_id : " + pay.getPrdIds().get(i));
            usrProcMapper.paySave(pay,usr_id);
            usrProcMapper.chatRoomSave(usr_id, pay.getPrdIds().get(i));

        }




    }


    public void usrDelete(String usr_id) {
        usrProcMapper.usrDelete(usr_id);
    }

    public void chatMassageSave(ResChatMessagesDto resChatMessagesDto) {
        usrProcMapper.chatMessagesSave(resChatMessagesDto);


        ResChatUsersDto resChatUsersDto = usrProcMapper.chatUsers(resChatMessagesDto.getChat_id());

        String opponent = Objects.equals(resChatUsersDto.getSell_users(), resChatMessagesDto.getUsr_id()) ? resChatUsersDto.getBuyer_users() : resChatUsersDto.getSell_users();

        opponent = usrProcMapper.pushToken(opponent);

        if (opponent != null) {
            fcmService.send_FCM(opponent, "채팅", resChatMessagesDto.getMsg());
        }


    }


    public void usrUpdate(ReqUsrUpdateDto reqUsrUpdateDto, String usrId) {

        for (int i = 0; i < reqUsrUpdateDto.getUpdateValue().size(); i++) {
            usrProcMapper.usrUpdate(reqUsrUpdateDto.getUpdateColumn().get(i), reqUsrUpdateDto.getUpdateValue().get(i), usrId);
        }
    }

    public void changeToWishList(List<Integer> prdIds, String usr_id) {
        System.out.println("prdIds" + prdIds);
        if (prdIds.size() > 1) {
            for (int prdId : prdIds) {
                System.out.println("prdId임" + prdId);
                usrProcMapper.wishListsDelete(prdId, usr_id);
            }

        } else {
            if (usrProcMapper.exitsByWishLists(usr_id, prdIds.get(0)) != null) {
                usrProcMapper.wishListsDelete(prdIds.get(0), usr_id);
            } else {
                usrProcMapper.wishListsSave(prdIds.get(0),usr_id);
            }
        }

    }


    public void reportDelete(List<Integer> rptIds) {
        for (int rptId : rptIds) {
            usrProcMapper.rptDelete(rptId);
        }

    }

    public void reviewDelete(List<Integer> prdIds, String usrId) {

        for (int prdId : prdIds) {
            usrProcMapper.reviewDelete(usrId,prdId);

        }
    }

    public void bidDel(List<Integer> prdIds, String usrId) {
        for (int prdId : prdIds) {
            usrProcMapper.auctionDelete(usrId, prdId);
        }

    }


    public void delDtmSave(ReqAddDelDtm reqAddDelDtm) {
        try {
          usrProcMapper.dealDtmSave(reqAddDelDtm);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deviceTokenSave(String token, String usrId) {
        if(usrProcMapper.exitsDeviceToken(usrId) == null){
            usrProcMapper.tokenSave(token, usrId);

        }

    }
}
