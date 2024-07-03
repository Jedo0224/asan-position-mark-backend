package org.asanpositioningserver.socket.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class SocketBaseResponse<T> {
    private MessageType messageType;
    private T data;

    public static <T> SocketBaseResponse<?> of(MessageType messageType, T data) {
        return SocketBaseResponse.builder()
                .messageType(messageType)
                .data(data)
                .build();
    }
}
