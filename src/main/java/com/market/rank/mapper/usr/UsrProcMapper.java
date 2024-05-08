package com.market.rank.mapper.usr;

import com.market.rank.dto.request.*;
import com.market.rank.dto.response.ResChatMessagesDto;
import com.market.rank.dto.response.ResChatUsersDto;
import com.market.rank.dto.response.ResPushDto;
import com.market.rank.dto.response.ResSinupDto;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UsrProcMapper {

    //user Insert
    @Insert("insert into usr(usr_id,usr_nm,mail,ph_num, bdate, pst_addr,rd_addr,det_addr )" +
            "values(to_char(sysdate, 'YYMMDD')||''||to_char(lpad(USER_SQ.nextval,2,'0')),#{usr_nm},#{mail},#{ph_num},#{bdate},#{pst_addr},#{rd_addr},#{det_addr})")
    void usrSave(ReqSignUpUsrDto reqSignUpUsrDto);

    @Select("SELECT usr_id, usr_nm, mail " +
            "FROM usr " +
            "where mail = #{mail} " )
    ResSinupDto usrLastSelect(String mail);


    //review Insert
    @Insert("insert into review(prd_id, usr_id, rev_des, rate_scr) values(#{prdId}, #{usrId}, #{revDes}, #{rateScr})")
    void reviewSave(ReqAddReview reqAddReview, String usr_id);

    //bidEnd
    @Insert("insert into win (usr_id, prd_id, win_price, win_dtm) " +
            "select a.usr_id, a.prd_id, a.bid_price, sysdate " +
            "from auction a " +
            "where a.bid_price= (select max(bid_price) " +
            "from auction " +
            "where prd_id= #{prdId}) and a.prd_id = #{prdId}")
    void winSave(int prdId);


    @Insert(" insert into win w (w.usr_id, w.prd_id, w.win_price, w.win_dtm)   +  " +
            "select a.usr_id, a.prd_id, a.bid_price, sysdate   +  " +
            "from auction a   +  " +
            "inner join product p on a.prd_id = p.prd_id   +  " +
            "where a.bid_price= (select max(subA.bid_price)   +  " +
            "from auction subA   +  " +
            "where subA.prd_id=p.prd_id) and a.prd_id = p.prd_id   +  " +
            "and TO_CHAR(p.end_dtm, 'YYYY/MM/DD HH24:MI:SS') = TO_CHAR(TRUNC(SYSDATE), 'YYYY/MM/DD') || ' 00:00:00' ")
    void autoWinSave();

    @Select("select d.token, p.title " +
            "from auction a " +
            "left join device_token d on d.usr_id = a.usr_id " +
            "left join product p on p.prd_id = a.prd_id " +
            "where a.bid_price = (select max(bid_price) from auction where prd_id = #{prd_id}) " +
            "and a.prd_id = #{prd_id}")
    ResPushDto pushBuyerInfo(int prd_id);


    @Select("select d.token from device_token d " +
            "left join  product p on p.usr_id = d.usr_id " +
            "where p.prd_id = #{prd_id}")
    String pushSellerInfo(int prd_id);

    @Update("update prdouct set bid_cnt = (select max(bid_cnt) from product where prd_id = #{prdId}) where prd_id = #{prdId}")
    void prdBidCntUpdate(int prdId);

    //rptSave
    @Insert("insert into rpt  values (to_char(sysdate, 'YYMMDD')||''||to_char(lpad(rpt_seq.nextval,2,'0')) ||''||#{rpt_id}, #{usr_id}, #{prd_id} , #{rpt_des})")
    void rptSave(ReqAddRptDto reqAddRptDto, String usr_id);


    //paySave
    @Insert("insert into pay(prd_id, usr_id, pay_prc, pay_dtm) values (#{prd_id}, #{usr_id}, #{pay_prc}, sysdate)")
    void paySave(ReqPayDto reqPayDto, String usr_id);


    @Insert("insert into chat_room(chat_id, usr_id, prd_id ) values (to_char(sysdate, 'YYMMDD')||''||to_char(lpad(chat_seq.nextval,2,'0')), #{usrId}, #{prdId} )")
    void chatRoomSave(String usr_id, int prd_id);

    //usrDelete
    @Delete("delete from usr where usr_id = #{usrId}")
    void usrDelete(String usr_id);

    //chatMassageSave
    @Insert("insert into chat_messages(chat_id, usr_id, msg, cre_dtm) values (#{chatId}, #{usrId}, #{msg}, sysdate)")
    void chatMessagesSave(ResChatMessagesDto resChatMessagesDto);

    @Select("select u.usr_id, (select usr_id from usr where usr_id = p.usr_id) " +
            "from chat_room c " +
            "left join product p on p.prd_id = c.prd_id " +
            "left join usr u on u.usr_id = c.usr_id " +
            "where c.chat_id = #{chat_id}")
    ResChatUsersDto chatUsers(String chat_id);

    @Select("select token from device_token where usr_id = #{usrId}")
    String pushToken(String usrId);

    //usrUpdate
    @Update("update usr set ${updateColumn} = #{updateValue} where usr_id = #{usrId}")
    void usrUpdate(String updateColumn,String updateValue, String usrId);

    //changeWishLists
    @Select("SELECT usr_id FROM wish_list WHERE usr_id = #{usrId} AND prd_id = #{prdId}")
    String exitsByWishLists(String usrId, int prdId);

    @Insert("insert into wish_lists(prd_id, usr_id) values (#{prdId}, #{usrId})")
    void wishListsSave(int prdId, String usrId);

    @Delete("delete from wish_lists where prd_id = #{prdId} and usr_id = #{usrId}")
    void wishListsDelete(int prdId, String usrId);


    //reportDelete
    @Delete("delete from rpt where rpt_id = #{rptId}")
    void rptDelete(int rptId);

    //reviewDelete
    @Delete("delete from review where usr_id = #{usrId} and prd_id = #{prdId}")
    void reviewDelete(String usrId, int prdId);

    //bidDel
    @Delete("delete from auction where prd_id = #{prdId} and usr_id = #{usrId}")
    void auctionDelete(String usrId, int prdId);

    //dealDtmSave
    @Insert(value = "insert into deal_dtm(prd_id, usr_id, lat, lng, dl_time) values(#{prdId}, #{usrId}, #{lat}, #{lng}, #{dlTime})")
    void dealDtmSave(ReqAddDelDtm reqAddDelDtm);

    //deviceTokenSave

    @Select("select token from device_token where usr_id = #{usrId}")
    String exitsDeviceToken(String usrId);

    @Insert("insert into device_token(usr_id, token) values(#{usrId}, #{token})")
    void tokenSave(String token, String usrId);

}
