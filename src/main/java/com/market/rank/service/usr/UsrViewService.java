package com.market.rank.service.usr;

import com.market.rank.dto.NaverSmsDto;
import com.market.rank.dto.request.ReqLoginUsrDto;
import com.market.rank.dto.response.*;
import com.market.rank.mapper.usr.UsrViewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsrViewService {

    private final UsrViewMapper usrViewMapper;


    private final RedisTemplate<String, String> redisTemplate;


    public List<ResRptDto> reportHistory(String usr_id) {
        return usrViewMapper.reportHistory(usr_id);
    }


    public ResChatRoomDto chatroom(String usrId) {
       ResChatRoomDto resChatRoomDto = usrViewMapper.chatRoom(usrId);

        resChatRoomDto.setUsr_id(usrId);

        return resChatRoomDto;
    }


    public ResUsrInfoDto searchUsrInfo(String email) {
        return usrViewMapper.usrCheckEmail(email);
    }

    public String searchRefreshToken(ReqLoginUsrDto requestUsrDto) {
        return redisTemplate.opsForValue().get(requestUsrDto.getUsrId());
    }

    public boolean usrCheck(NaverSmsDto naverSmsDto) {
        return usrViewMapper.usrCheckPhNum(naverSmsDto.getPh_num()) == null;
    }


    public boolean codeCheck(NaverSmsDto naverSmsDto) {
        String code = redisTemplate.opsForValue().get(naverSmsDto.getPh_num());
        return code != null && code.equals(naverSmsDto.getPh_code());
    }

    public String usrPhNum(String usr_id) {
        return usrViewMapper.findByPhNum(usr_id);
    }


    public List<ResWishDto> usrWishLists(String usrId) {
        return usrViewMapper.resWishList(usrId);
    }

    public List<ResPrdmgmtDto> prdmgmt(String usrId) {
        return usrViewMapper.resPrdMgMt(usrId);
    }


    public List<ResBidDto> bidhistory(String usrId) {
        return usrViewMapper.resBids(usrId);
    }


    public List<ResWinDto> winHistory(String usr_id) {
        return usrViewMapper.resWins(usr_id);
    }


    public List<ResPayDto> payHistory(String usr_id, int status) {
        if(status == 1){
            return usrViewMapper.resEndPays(usr_id);
        }
        return usrViewMapper.resPays(usr_id);
    }


    public List<ResChatMessagesDto> chatMessages(int pageNumber, String chat_id) {
        return usrViewMapper.resChatMessages(chat_id, pageNumber);
    }

    public List<ResAddrDto> chatAddress(String chat_id) {
        return usrViewMapper.chatAddress(chat_id);
    }

    public ResUsrInfoDto usrInfo(String usr_id) {
        return usrViewMapper.resUsrInfo(usr_id);
    }

    public List<ResReviewDto> myReviewHistory(String usr_id) {
        return usrViewMapper.resReviews(usr_id);
    }


    public List<ResReviewDto> myPrdReviewHistory(String usr_id) {
        return usrViewMapper.resMyReviews(usr_id);
    }

}