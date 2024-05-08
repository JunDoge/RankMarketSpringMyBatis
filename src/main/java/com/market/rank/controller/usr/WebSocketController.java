package com.market.rank.controller.usr;

import com.market.rank.dto.response.ResChatMessagesDto;
import com.market.rank.service.usr.UsrProcService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ConcurrentHashMap<String, String> session = new ConcurrentHashMap<String, String>();
    private final UsrProcService usrProcService;

    @EventListener(SessionConnectEvent.class)
    public void onConnect(SessionConnectEvent event){
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(event.getMessage(), StompHeaderAccessor.class);
        String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();
        if (headerAccessor != null) {
            List<String> userHeaders = headerAccessor.getNativeHeader("User");
            if (userHeaders != null && !userHeaders.isEmpty()) {
                String userId = userHeaders.get(0);
                session.put(sessionId, userId);

            }
        }
    }

    @EventListener(SessionDisconnectEvent.class)
    public void onDisconnect(SessionDisconnectEvent event) {
        session.remove(event.getSessionId());
    }

    @MessageMapping("/add/chatroom/message")
    public void sendMessage(@DestinationVariable String roomId, ResChatMessagesDto resChatMessagesDto, SimpMessageHeaderAccessor accessor) {

        String sessionId = accessor.getSessionId();
        String writerId = session.get(sessionId);

        resChatMessagesDto.setUsr_id(writerId);
        usrProcService.chatMassageSave(resChatMessagesDto);
        simpMessagingTemplate.convertAndSend("/sub/chatroom/" + roomId, resChatMessagesDto);
    }
}