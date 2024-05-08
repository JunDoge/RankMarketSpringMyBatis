package com.market.rank.controller.usr;

import com.market.rank.dto.response.ResDto;
import com.market.rank.service.usr.UsrViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UsrViewController {


    private final UsrViewService usrService;


    @GetMapping("/wishes")
    public ResponseEntity<ResDto> usrWishList() {
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResDto.builder()
                                .status(HttpStatus.OK)
                                .response(usrService.usrWishLists(usr_id))
                                .build()
                );
    }

    @GetMapping("/mypage/history/product")
    public ResponseEntity<ResDto> prdMgmt() {
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResDto.builder()
                        .status(HttpStatus.OK)
                        .response(
                                usrService.prdmgmt(usr_id)
                        ).build());
    }

    @GetMapping("/mypage/history/bid")
    public ResponseEntity<ResDto> bidHistory() {
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();


        return ResponseEntity.status(HttpStatus.OK)
                .body(ResDto.builder()
                        .status(HttpStatus.OK)
                        .response(
                                usrService.bidhistory(usr_id)
                        ).build());

    }

    @GetMapping("/mypage/history/win")
    public ResponseEntity<ResDto> winHistory() {
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();


        return ResponseEntity.status(HttpStatus.OK)
                .body(ResDto.builder()
                        .status(HttpStatus.OK)
                        .response(
                                usrService.winHistory(usr_id)
                        ).build());
    }

    @GetMapping("/mypage/history/pay")
    public ResponseEntity<ResDto> payHistory(@RequestParam("status") int status) {
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResDto.builder()
                        .status(HttpStatus.OK)
                        .response(
                                usrService.payHistory(usr_id,status)
                        ).build());
    }

    @GetMapping("/mypage/history/report")
    public ResponseEntity<ResDto> reporthistory() {
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResDto.builder()
                        .status(HttpStatus.OK)
                        .response(
                                usrService.reportHistory(usr_id)
                        ).build());
    }


    @GetMapping("/chatroom")
    public ResponseEntity<ResDto> chatroom() {
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();


        return ResponseEntity.status(HttpStatus.OK)
                .body(ResDto.builder()
                        .status(HttpStatus.OK)
                        .response(
                                usrService.chatroom(usr_id)
                        ).build());
    }

    @GetMapping("/mypage/history/review")
    public ResponseEntity<ResDto> reviewHistory() {
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResDto.builder()
                        .status(HttpStatus.OK)
                        .response(
                                usrService.myReviewHistory(usr_id)
                        ).build());
    }

    @GetMapping("/mypage/history/myreview")
    public ResponseEntity<ResDto> myPrdReviewHistory() {
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResDto.builder()
                        .status(HttpStatus.OK)
                        .response(
                                usrService.myPrdReviewHistory(usr_id)
                        ).build());
    }


    @GetMapping("/chatroom/add/message")
    public ResponseEntity<ResDto> chatMessage(@RequestParam(value = "pageNum") int pageNumber,
                                              @RequestParam(value = "chat_id") String chat_id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResDto.builder()
                        .status(HttpStatus.OK)
                        .response(
                                usrService.chatMessages(pageNumber, chat_id)
                        ).build());
    }

    @PostMapping("/mypage/info/usr")
    public ResponseEntity<ResDto> usrInfo() {
        String usr_id = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResDto.builder()
                                .status(HttpStatus.OK)
                                .response(
                                        usrService.usrInfo(usr_id)
                                )
                                .build()
                );
    }

}