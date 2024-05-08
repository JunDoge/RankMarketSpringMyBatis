package com.market.rank.service.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.rank.api.naver.NaverSmsMessages;
import com.market.rank.api.naver.NaverSmsRequest;
import com.market.rank.api.naver.NaverSmsResponse;
import com.market.rank.config.api.NaverSmsProperties;
import com.market.rank.dto.NaverSmsDto;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NaverSmsService {


    private final NaverSmsProperties naverSmsProperties;
    public String getSignature(String time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + naverSmsProperties.getServiceId() + "/messages";
        String accessKey = naverSmsProperties.getAccessKey();
        String secretKey = naverSmsProperties.getSecretKey();

        String message = method +
                space +
                url +
                newLine +
                time +
                newLine +
                accessKey;

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeBase64String(rawHmac);
    }

    public NaverSmsResponse sendSms(NaverSmsDto naverSmsDto) {
        try {


            String time = Long.toString(System.currentTimeMillis());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-ncp-apigw-timestamp", time);
            headers.set("x-ncp-iam-access-key", naverSmsProperties.getAccessKey());
            headers.set("x-ncp-apigw-signature-v2", getSignature(time));

            int min = 1000;
            int max = 9999;
            int validateNum = (int) (Math.random() * (max - min + 1)) + min;

            naverSmsDto.setPh_code(Integer.toString(validateNum));

            List<NaverSmsMessages> naverMessages = new ArrayList<>();
            naverMessages.add(NaverSmsMessages.builder()
                                    .to(naverSmsDto.getPh_num())
                                    .build());

            NaverSmsRequest request = NaverSmsRequest.builder()
                    .type("SMS")
                    .contentType("COMM")
                    .countryCode("82")
                    .from(naverSmsProperties.getFrom())
                    .content("랭크마켓 인증번호는: " + validateNum)
                    .messages(naverMessages)
                    .build();

            //쌓은 바디를 json형태로 반환
            ObjectMapper objectMapper = new ObjectMapper();
            String body = objectMapper.writeValueAsString(request);
            // jsonBody와 헤더 조립
            HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

            System.out.println(httpBody.getBody());

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());



            return restTemplate.postForObject(
                    new URI("https://sens.apigw.ntruss.com/sms/v2/services/"
                    + naverSmsProperties.getServiceId()
                    +"/messages"),
                    httpBody,
                    NaverSmsResponse.class);



        }catch (Exception e) {
            return null;
        }

    }

}
