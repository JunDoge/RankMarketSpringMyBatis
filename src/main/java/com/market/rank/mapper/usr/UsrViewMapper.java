package com.market.rank.mapper.usr;

import com.market.rank.dto.response.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UsrViewMapper {
    @Select("select max(f.fl_id) as img, r.rpt_des, r.rpt_id, SUBSTR(r.rpt_id, 1, 1) as rpt_type " +
            "from rpt r " +
            "left join files f on f.prd_id = r.prd_id " +
            "where r.usr_id = #{usrId} " +
            "group by r.rpt_des, r.usr_id, r.rpt_id")
    List<ResRptDto> reportHistory(String usrId);

    @Select("select c.chat_id as chat_id , p.title as prd_title " +
            "from chat_room c " +
            "left join product p on p.prd_id = c.prd_id " +
            "left join usr u on u.usr_id = c.usr_id " +
            "where c.usr_id = #{usrId} and p.usr_id = #{usrId}")
    ResChatRoomDto chatRoom(String usrId);

    @Select("select usr_id as usrId, usr_nm as usrNm, mail as mail from usr where mail = #{email}")
    ResUsrInfoDto usrCheckEmail(String email);

    @Select("select usr_id from usr where ph_num = #{phNum}")
    String usrCheckPhNum(String phNum);


    @Select("select ph_num from usr where usr_id = #{usrId}")
    String findByPhNum(String usrId);

    @Select("select case when p.bid_cnt = 0 then p.sell_prc else max(a.bid_price) end," +
            "p.ieast_prc as ieast_price, " +
            "p.high_prc as high_price, " +
            "p.title as title, " +
            "max(f.fl_id) as img, " +
            "p.prd_id as prd_id, " +
            "to_char(p.end_dtm, 'yyyy-MM-dd hh:mm') " +
            "from product p " +
            "left join files f on f.prd_id = p.prd_id " +
            "left join auction a on a.prd_id = p.prd_id " +
            "left join wish_lists w on w.prd_id = p.prd_id " +
            "where w.usr_id = #{usrId} " +
            "group by p.title, p.prd_id, p.end_dtm, p.sell_prc, p.bid_cnt, p.high_prc, p.ieast_prc")

    List<ResWishDto> resWishList(String usrId);

    @Select("select p.title as title, p.prd_id as prd_id, p.high_prc, p.ieast_prc, " +
            "to_char(p.end_dtm, 'yyyy-MM-dd hh:mm'), " +
            "case when p.bid_cnt = 0 then p.sell_prc else max(a.bid_price) end as bid_price, " +
            "p.bid_cnt as bid_cnt " +
            "from product p " +
            "left join auction a on a.prd_id = p.prd_id " +
            "where p.usr_id = #{usrId} " +
            "group by p.title, p.prd_id, p.high_prc, p.ieast_prc, p.end_dtm, p.status, p.sell_prc, p.bid_cnt")
    List<ResPrdmgmtDto> resPrdMgMt(String usrId);



    @Select("select max(f.fl_id) as img, to_char(a.bid_dtm, 'yyyy-MM-dd hh:mm')," +
            "a.bid_price as bid_price, " +
            "p.prd_id as prdId, " +
            "p.sell_prc as sellPrice, " +
            "p.high_prc as highPrice, " +
            "p.ieast_prc as ieastPrice, " +
            "p.title as title, " +
            "p.status as status " +
            "from product p " +
            "left join auction a on a.prd_id = p.prd_id " +
            "left join files f on f.prd_id = a.prd_id " +
            "where a.usr_id = #{usrId} " +
            "group by a.bid_dtm, a.bid_price, p.prd_id, p.end_dtm, p.end_dtm, p.sell_prc, p.high_prc, p.ieast_prc, p.title, p.status")
    List<ResBidDto> resBids(String usrId);


    @Select("select max(f.fl_id) as img, w.win_price as winPrice," +
            "case when py.pay_dtm is null then 1 " +
            "else (case when rp.usr_id is null and rv.usr_id is null then 2 " +
            "else (case when rp.usr_id is not null and rv.usr_id is null then 2 else 3 end) end) " +
            "end as status," +
            "p.prd_id as prdId," +
            "p.sell_prc as sellPrice," +
            "p.title as title, " +
            "p.high_prc as highPrice " +
            "from win w  " +
            "left join product p on p.prd_id = w.prd_id " +
            "left join files f on f.prd_id = w.prd_id " +
            "left join review rv on rv.prd_id = w.prd_id " +
            "left join rpt rp on rp.prd_id = w.prd_id " +
            "left join pay  py on py.prd_id = w.prd_id " +
            "where w.usr_id = #{usrId} " +
            "group by w.win_price, p.prd_id, p.sell_prc, p.title, p.high_prc, rv.usr_id, rp.usr_id, py.pay_dtm")

    List<ResWinDto> resWins(String usrId);


    @Select("select max(f.fl_id) as img, p.title as title, w.win_price as win_price, p.prd_id as prd_id," +
            "to_char(py.pay_dtm, 'yyyy-MM-dd hh:mm')," +
            "case when to_char(py.pay_dtm, 'yyyy-MM-dd hh:mm') is null then 0 else 1 end " +
            "from product p " +
            "left join win w on w.prd_id = p.prd_id " +
            "left join pay py on py.prd_id = p.prd_id " +
            "left join files f on f.prd_id = p.prd_id " +
            "where w.usr_id = #{usrId} " +
            "group by w.win_price, py.pay_dtm, p.title, p.prd_id")
    List<ResPayDto> resPays(String usrId);


    @Select("select max(f.fl_id) as img, p.title as title, w.win_price as win_price, p.prd_id as prd_id," +
            "to_char(py.pay_dtm, 'yyyy-MM-dd hh:mm')," +
            "case when to_char(py.pay_dtm, 'yyyy-MM-dd hh:mm') is null then 0 else 1 end " +
            "from product p " +
            "left join win w on w.prd_id = p.prd_id " +
            "left join pay py on py.prd_id = p.prd_id " +
            "left join files f on f.prd_id = p.prd_id " +
            "where w.usr_id = #{usrId} and py.prd_id is null " +
            "group by w.win_price, py.pay_dtm, p.title, p.prd_id")
    List<ResPayDto> resEndPays(String usrId);


    @Select("select usr_id as usr_id , msg as msg, cre_dtm as cre_dtm, c.chat_id as chat_id " +
            "from chat_messages " +
            "where chat_id = #{chatId} " +
            "order by cre_dtm desc " +
            "OFFSET #{pageNumber} ROWS FETCH NEXT 20 ROWS ONLY")
    List<ResChatMessagesDto> resChatMessages(String chatId, int pageNumber);

    @Select("select u.rd_addr as buyer," +
            "(select rd_addr from usr where usr_id = p.usr_id) as seller," +
            "p.prd_id as prd_id, " +
            "case when u.usr_id = #{usrId} then '' else u.usr_id end " +
            "from chat_room c " +
            "left join product p on p.prd_id = c.prd_id " +
            "left join usr u on u.usr_id - c.usr_id " +
            "where c.chat_id = #{chatId}")
    List<ResAddrDto> chatAddress(String chatId);

    @Select("select rd_addr, pst_addr, mail, ph_num, det_addr, usr_nm from usr where usr_id = #{usrId}")
    ResUsrInfoDto resUsrInfo(String usrId);


    @Select("select max(f.fl_id) as img, r.prd_id as prd_id, r.rev_des as revDes, r.rate_scr as rate_scr " +
            "from review r " +
            "left join files f on f.prd_id = r.prd_id " +
            "where r.usr_id = #{usrId} " +
            "group by r.rev_des, r.usr_id, r.rate_scr, r.prd_id")
    List<ResReviewDto> resReviews(String usrId);

    @Select("select max(f.fl_id) as img, r.prd_id as prd_id, r.rev_des as revDes, r.rate_scr as rate_scr " +
            "from review " +
            "left join files f on f.prd_id = r.prd_id " +
            "where r.prd_id = (select prd_id from product where usr_id = #{usrId}) " +
            "group by r.rev_des, r.usr_id, r.rate_scr, r.prd_id")
    List<ResReviewDto> resMyReviews(String usrId);


}
