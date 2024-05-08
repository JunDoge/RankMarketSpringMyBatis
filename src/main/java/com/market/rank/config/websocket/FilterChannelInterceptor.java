package com.market.rank.config.websocket;

import com.market.rank.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Configuration
@RequiredArgsConstructor
public class FilterChannelInterceptor implements ChannelInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert headerAccessor != null;
        if (headerAccessor.getCommand() == StompCommand.CONNECT) {
            String token = String.valueOf(headerAccessor.getNativeHeader("Authorization").get(0));
            token = token.substring(7);
            try {
                String usr_id = tokenProvider.getUsrId(token);
                headerAccessor.addNativeHeader("User", String.valueOf(usr_id));
            } catch (Exception ignored) {

            }
        }
        return message;
    }
}