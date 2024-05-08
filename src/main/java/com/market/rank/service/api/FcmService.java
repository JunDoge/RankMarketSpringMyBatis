package com.market.rank.service.api;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;

@Service
public class FcmService {
    public void send_FCM(String tokenId, String title, String content) {
        try {
            //본인의 json 파일 경로 입력
            FileInputStream refreshToken = new FileInputStream("C:\\Users\\b\\Desktop\\rank\\src\\main\\resources\\fcm\\flutter-rankmarket-firebase-adminsdk-fwdt7-63af153e78.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
                    .setDatabaseUrl("firebase-adminsdk-fwdt7@flutter-rankmarket.iam.gserviceaccount.com")
                    .build();

            //Firebase 처음 호출시에만 initializing 처리
            if(FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            //String registrationToken = 안드로이드 토큰 입력;
            String registrationToken = tokenId;

            //message 작성
            Message msg = Message.builder()
                    .setAndroidConfig(AndroidConfig.builder()
                            .setTtl(3600 * 1000) // 1 hour in milliseconds
                            .setPriority(AndroidConfig.Priority.NORMAL)
                            .setNotification(AndroidNotification.builder()
                                    .setTitle(title)
                                    .setBody(content)
                                    .setIcon("stock_ticker_update")
                                    .setColor("#f45342")
                                    .build())
                            .build())
                    .setToken(registrationToken)
                    .build();

            //메세지를 FirebaseMessaging 에 보내기
            String response = FirebaseMessaging.getInstance().send(msg);
            //결과 출력
            System.out.println("Successfully sent message: " + response);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
