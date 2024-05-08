package com.market.rank.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ReqAddPrdDto {
    private List<MultipartFile> images;
    private String usrId;
    private String title;
    private int sellPrice;
    private int highPrice;
    private String catId;
    private int iseatPrice;
    private String significant;
    private String des;

}
