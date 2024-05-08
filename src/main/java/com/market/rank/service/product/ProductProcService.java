package com.market.rank.service.product;

import com.market.rank.dto.request.ReqAddAuctionDto;
import com.market.rank.dto.request.ReqAddPrdDto;
import com.market.rank.dto.request.ReqUpdatePrdDto;
import com.market.rank.dto.response.ResPushDto;
import com.market.rank.mapper.product.ProductProcMapper;
import com.market.rank.mapper.product.ProductViewMapper;
import com.market.rank.mapper.usr.UsrProcMapper;
import com.market.rank.service.api.AwsService;
import com.market.rank.service.api.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductProcService {

    private final AwsService awsService;
    private final ProductProcMapper productProcMapper;
    private final ProductViewMapper productViewMapper;
    private final UsrProcMapper usrProcMapper;
    private final FcmService fcmService;

    public void productAdd(ReqAddPrdDto reqAddPrdDto) {

       productProcMapper.prdSave(reqAddPrdDto);

       int prd_id = productProcMapper.lastPrdId();


        for (MultipartFile multipart : reqAddPrdDto.getImages()) {
            String fileId = awsService.uploadToAWS(multipart);
            productProcMapper.filesSave(fileId,prd_id);
        }


    }


    public void auctionAdd(ReqAddAuctionDto reqAddAuctionDto) {
        int prices = productProcMapper.auctionPrice(reqAddAuctionDto.getPrdId());

        if (!(reqAddAuctionDto.getHighPrice() < (prices + reqAddAuctionDto.getIeastPrice()))) {
            reqAddAuctionDto.setIeastPrice(reqAddAuctionDto.getIeastPrice() + prices);
            productProcMapper.auctionSave(reqAddAuctionDto);
            productProcMapper.updateBidCnt(reqAddAuctionDto.getPrdId());

            if ((reqAddAuctionDto.getHighPrice() - (prices + (reqAddAuctionDto.getIeastPrice() * 2))) < 0) {
               usrProcMapper.winSave(reqAddAuctionDto.getPrdId());


                ResPushDto buyerInfo = usrProcMapper.pushBuyerInfo(reqAddAuctionDto.getPrdId());

                String sellerInfo = usrProcMapper.pushSellerInfo(reqAddAuctionDto.getPrdId());

                if (buyerInfo.getToken() != null) {
                    fcmService.send_FCM(buyerInfo.getToken(), "랭크마켓", buyerInfo.getTitle() + " 상품이 낙찰되었습니다.");
                }
                if (sellerInfo != null) {
                    fcmService.send_FCM(sellerInfo, "랭크마켓", buyerInfo.getTitle() + " 상품이 낙찰되었습니다.");
                }


            }


        }


    }

    public void prdUpdate(ReqUpdatePrdDto updatePrdDto) {
        productProcMapper.updateProduct(updatePrdDto);

    }
    public void prdDelete(int prdId) {
        List<String> files = productViewMapper.prdImgs(prdId);
        for (String file : files) {
            awsService.deleteToAWS(file);
        }

       productProcMapper.deleteProduct(prdId);
    }



}
