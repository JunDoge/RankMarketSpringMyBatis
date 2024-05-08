package com.market.rank.service.api;

import com.market.rank.dto.request.ReqPayDto;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;

@Service
public class NurigoService {

    final DefaultMessageService messageService;

    public NurigoService() {
        this.messageService = NurigoApp.INSTANCE.initialize("NCSSAHW6IABNWJSZ", "8TOPHAZRWGV1GDOLUTPBNQXPEY2JCRZT", "https://api.coolsms.co.kr");
    }

    public void sendOne(ReqPayDto reqPayDto, String ph_num) {


        System.out.println(ph_num);
        System.out.println(reqPayDto.getTitle());
        System.out.println(reqPayDto.getPayPrice());

        int price = 0;
        for(int i : reqPayDto.getPayPrice()){
            price = i;
        }

        System.out.println("상품이 무사히 결제되었습니다.\n상품명 : "+reqPayDto.getTitle() +"\n가격 : "+price);

        Message message = new Message();
        message.setFrom("01097192320");
        message.setTo(ph_num);
        message.setText("상품이 무사히 결제되었습니다.\n상품명 : "+reqPayDto.getTitle() +"\n가격 : "+price);
        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

    }


}
