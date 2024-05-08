package com.market.rank.controller.product;

import com.market.rank.dto.request.ReqAddAuctionDto;
import com.market.rank.dto.request.ReqAddPrdDto;
import com.market.rank.dto.request.ReqRankingCheckDto;
import com.market.rank.dto.request.ReqUpdatePrdDto;
import com.market.rank.dto.response.ResDto;
import com.market.rank.service.api.RankingCheckService;
import com.market.rank.service.product.ProductProcService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductProcController {

    private final RankingCheckService rankingCheckService;

    private final ProductProcService productProcService;

    @PostMapping(value = "/rankingCheck", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResDto> uploadImages(@RequestBody() ReqRankingCheckDto checkDto) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResDto.builder()
                                        .status(HttpStatus.OK)
                                        .response(
                                                rankingCheckService.uploadImage(checkDto)
                                        ).build()
                        );

    }

    @PostMapping(value = "/add/product", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void productAdd(ReqAddPrdDto reqAddPrdDto){
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();
        reqAddPrdDto.setUsrId(usr_id);
        productProcService.productAdd(reqAddPrdDto);

    }


    @PostMapping(value = "/bid/product")
    public void productBidding(ReqAddAuctionDto reqAddAuctionDto){
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();
        reqAddAuctionDto.setUsrId(usr_id);
        productProcService.auctionAdd(reqAddAuctionDto);
    }

    @PutMapping(value = "update/product", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void productAdd(ReqUpdatePrdDto updatePrdDto){
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();
        updatePrdDto.setUsrId(usr_id);
        productProcService.prdUpdate(updatePrdDto);

    }


    @DeleteMapping(value = "delete/product/{prdId}")
    public void productDelete(@PathVariable("prdId") int prd_id){
        productProcService.prdDelete(prd_id);
    }



}
