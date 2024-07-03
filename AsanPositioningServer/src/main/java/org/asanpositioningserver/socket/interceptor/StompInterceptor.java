package org.asanpositioningserver.socket.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.asanpositioningserver.domain.position.entity.Position;
import org.asanpositioningserver.domain.position.mongorepository.PositionMongoRepository;
import org.asanpositioningserver.socket.error.SocketException;
import org.asanpositioningserver.socket.error.SocketNotFoundException;
import org.asanpositioningserver.socket.error.SocketUnauthorizedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.asanpositioningserver.socket.error.SocketErrorCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor  {
    public final static Long monitoringId = 9999999L;
//    private final WatchRepository watchRepository;
//    private final SensorDataRepository sensorDataRepository;
    private final PositionMongoRepository positionMongoRepository;
//    private final WatchLiveRepository watchLiveRepository;
//    private final SensorScheduler sensorScheduler;
    @Qualifier("taskScheduler")
    private final TaskScheduler taskScheduler;

    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();




    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        if (StompCommand.CONNECT.equals(command)) {
            Long watchId = getWatchByAuthorizationHeader(accessor);
            setWatchIdFromStompHeader(accessor, watchId);
            log.info("[CONNECT]:: watchId : " + watchId);



        }
        if (StompCommand.DISCONNECT.equals(command)){
            Long watchId = (Long) getWatchIdFromStompHeader(accessor);
            deleteWatchIdFromStompHeader(accessor);
            log.info("DISCONNECTED watchId : {}", watchId);
        }

        log.info(String.valueOf(command));

        return message;
    }



    private Long getWatchByAuthorizationHeader(StompHeaderAccessor accessor) {
        String authHeaderValue = accessor.getFirstNativeHeader("Authorization");
        long longAuthHeaderValue = Long.parseLong(Objects.requireNonNull(authHeaderValue));
        if (longAuthHeaderValue != monitoringId) {

        }
        return longAuthHeaderValue;
    }

    private Object getWatchIdFromStompHeader(StompHeaderAccessor accessor) {
        Map<String, Object> sessionAttributes = getSessionAttributes(accessor);
        Object value = sessionAttributes.get("watchId");
//        if (Objects.isNull(value))
//            throw new SocketException(SOCKET_SERVER_ERROR);
        return value;
    }

    private void setWatchIdFromStompHeader(StompHeaderAccessor accessor, Object value) {
        Map<String, Object> sessionAttributes = getSessionAttributes(accessor);
        if (Objects.isNull(sessionAttributes)) return;
        sessionAttributes.put("watchId", value);
    }

    private void deleteWatchIdFromStompHeader(StompHeaderAccessor accessor) {
        Map<String, Object> sessionAttributes = getSessionAttributes(accessor);
        if (Objects.isNull(sessionAttributes)) return;
        sessionAttributes.remove("watchId");
    }

    private Map<String, Object> getSessionAttributes(StompHeaderAccessor accessor) {
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (Objects.isNull(sessionAttributes))
            return null;
        return sessionAttributes;
    }




}

