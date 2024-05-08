package com.market.rank.mapper.product;

import com.market.rank.dto.request.ReqAddAuctionDto;
import com.market.rank.dto.request.ReqAddPrdDto;
import com.market.rank.dto.request.ReqUpdatePrdDto;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ProductProcMapper {
    @Insert("INSERT INTO product (prd_id, cat_id, usr_id, title, sell_prc, high_prc, significant, ieast_prc, des, end_dtm, start_dtm, status) " +
            "VALUES (PRD_SEQ.nextval, #{cat_id},#{usrId}, #{title}, #{sell_prc}, #{high_prc}, #{significant}, #{ieast_price}, #{des},sysdate + 7, TRUNC(sysdate), 1)")
    void prdSave(ReqAddPrdDto reqAddPrdDto);

    @Select("select max(prd_id) from product")
    int lastPrdId();


    @Select("select case when p.bid_cnt = 0 " +
            "then p.sell_prc else max(a.bid_price) end " +
            "from product p " +
            "left join auction a on p.prd_id = a.prd_id " +
            "where p.prd_id = #{prdId}")
    int auctionPrice(int prdId);


    @Update("update product set bid_cnt = " +
            "(select ( bid_cnt +1 ) from product where prd_id = #{prdId}) where prd_id = #{prdId}")

    void updateBidCnt(int prdId);


    @Insert("insert into auction(prd_id, usr_id, bid_price) values (#{prdId}, #{usrId}, #{ieastPrice})")
    void auctionSave(ReqAddAuctionDto addAuctionDto);

    @Insert("INSERT INTO files (fl_id, prd_id) VALUES (#{fileId}, #{prdId});")
    void filesSave(String fileId, int prdId);

    @Update("UPDATE product SET title = #{title}, significant = #{significant}, ieast_prc = #{ieast_prc}, des = #{des} "
            + "WHERE prd_id = #{prdId} AND usr_id = #{usrId};")
    void updateProduct(ReqUpdatePrdDto reqAddPrdDto);

    @Delete("DELETE FROM product WHERE prd_id = #{prdId}")
    void deleteProduct(int prdId);


    @Select("select price from cat where cat_id = #{catId}")
    int catPrice(String catId);


}
