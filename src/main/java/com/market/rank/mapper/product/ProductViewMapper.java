package com.market.rank.mapper.product;

import com.market.rank.dto.response.ResPrdDto;
import com.market.rank.dto.response.ResPrdMostDto;
import com.market.rank.dto.response.ResPrdPopDto;
import com.market.rank.dto.response.ResPrdsDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductViewMapper {

    @Select("select " +
       "p.title as title, " +
       "p.prd_id as prdId," +
       "case when p.bid_cnt = 0 then p.sell_prc else max(a.bid_price) end as sellPrice," +
       "to_char(p.end_dtm, 'yyyy-MM-dd hh:mm') as endDtm, " +
       "case when (select usr_id from wish_lists where usr_id = #{usrId} and prd_id = p.prd_id) is null " +
       "then 0 else 1 end as wish, " +
       "p.high_prc as highPrice, " +
       "max(f.fl_id) as img " +
       "from product p " +
       "left join files f on f.prd_id = p.prd_id " +
       "left join auction a on a.prd_id = p.prd_id " +
       "group by p.title, p.prd_id, p.sell_prc, p.end_dtm, p.high_prc, p.bid_cnt, p.sell_prc " +
       "order by p.end_dtm desc " +
       "OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY")
    List<ResPrdsDto> productViews(String usrId,int offset);

    @Select("select " +
            "p.title as title, " +
            "p.prd_id as prd_id,     " +
            "case when p.bid_cnt = 0 then p.sell_prc else max(a.bid_price) end as sell_price," +
            "to_char(p.end_dtm, 'yyyy-MM-dd hh:mm') as endDtm, " +
            "case when (select usr_id from wish_lists where usr_id = #{usrId} and prd_id = p.prd_id) is null " +
            "then 0 else 1 end as wish, " +
            "p.high_prc as high_price, " +
            "max(f.fl_id) as img " +
            "from product p " +
            "left join files f on f.prd_id = p.prd_id " +
            "left join auction a on a.prd_id = p.prd_id " +
            "where p.title like '%' + #{search} + '%' " +
            "group by p.title, p.prd_id, p.sell_prc, p.end_dtm, p.high_prc, p.bid_cnt, p.sell_prc " +
            "order by p.end_dtm desc " +
            "OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY")
    List<ResPrdsDto> productSearchViews(String userId,int offset, String search);





    @Select("select p.title, case when p.bid_cnt = 0 then p.sell_prc else max(a.bid_price) end as bid_price," +
       "p.sell_prc as sell_price," +
       "p.high_prc as high_price," +
       "p.ieast_prc as ieast_price," +
       "p.significant as significant," +
       "p.prd_id as prd_id," +
       "p.des as des," +
       "to_char(p.end_dtm, 'yyyy-MM-dd hh:mm')" +
       "from product p " +
       "left join auction a on a.prd_id = p.prd_id " +
       "where p.prd_id = #{prdId} " +
       "group by p.title, p.des, p.high_prc, p.sell_prc, p.ieast_prc, p.end_dtm, p.bid_cnt, p.prd_id, p.significant;")
    ResPrdDto productView(int prdId);

    @Select("select p.prd_id as prd_id, p.title as title, p.ieast_prc as ieast_price, p.high_prc as high_price, max(f.fl_id) as img " +
       "from product p " +
       "left join files f on f.prd_id = p.prd_id " +
       "where p.end_dtm > sysdate " +
       "group by p.title, p.prd_id, p.ieast_prc, p.bid_cnt, p.high_prc, p.end_dtm " +
       "order by p.bid_cnt desc " +
       "OFFSET 0 ROWS FETCH NEXT 4 ROWS ONLY")
    List<ResPrdPopDto> prdPopular();


    @Select("select p.prd_id as prd_id, p.title as title, p.ieast_prc as ieast_price, p.high_prc as high_price " +
       "from product p  " +
       "left join files f on f.prd_id = p.prd_id  " +
       "where p.end_dtm > sysdate  " +
       "order by p.end_dtm desc  " +
       "OFFSET 0 ROWS FETCH NEXT 3 ROWS ONLY")
    List<ResPrdMostDto> prdMost();

    @Select("select fl_id as imgs from files where prd_id = #{prdId}")
    List<String> prdImgs(int prdId);


}






