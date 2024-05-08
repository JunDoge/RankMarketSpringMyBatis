package com.market.rank.controller.product;

import com.market.rank.dto.response.ResDto;
import com.market.rank.service.product.ProductViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductViewController {
    private final ProductViewService productViewService;
    @GetMapping("/products")
    public ResponseEntity<ResDto> products(@RequestParam(name = "pageNum", required = false, defaultValue = "'0'") int offset,
                                           @RequestParam(value = "prdNm", required=false) String search)  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usr_id = "";
        if (authentication != null && authentication.isAuthenticated()) {
            usr_id = authentication.getName();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResDto.builder()
                        .status(HttpStatus.OK)
                        .response(
                                productViewService.products(offset,usr_id,search)
                        ).build());
    }

    @GetMapping("/product/{prdId}")
    public ResponseEntity<ResDto> product(@PathVariable("prdId") int prdId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResDto.builder()
                        .status(HttpStatus.OK)
                        .response(
                                productViewService.product(prdId)
                        ).build());
    }


    @GetMapping("/popular/main")
    public ResponseEntity<ResDto> popular(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResDto.builder()
                        .status(HttpStatus.OK)
                        .response(
                                productViewService.prdPopular()
                        ).build());
    }
    @GetMapping("/most/main")
    public ResponseEntity<ResDto> recent(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResDto.builder()
                                .status(HttpStatus.OK)
                                .response(
                                        productViewService.prdMost()
                                ).build()
                );
    }

}
