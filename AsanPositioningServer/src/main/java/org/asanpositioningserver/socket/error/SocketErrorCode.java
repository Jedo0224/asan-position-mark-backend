package org.asanpositioningserver.socket.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SocketErrorCode {

    /**
     * 1401 UnAuthorized
     */
    WATCH_UNAUTHORIZED(1401, "접근할 수 있는 권한이 없습니다."),
    BLINK_AUTHORIZED_HEADER(1401, "인증 헤더가 비어있습니다."),


    /**
     * 1404 Not found
     */
    SESSION_ATTRIBUTE_NOT_FOUND(1404, "SessionAttributes를 찾을 수 없습니다."),
    SESSION_ATTRIBUTE_NULL_VALUE(1404, "세션 속성값이 null입니다."),
    WATCH_NOT_FOUND(1404, "등록된 워치가 없습니다.."),

    /**
     * 1500 Socket Server
     */
    SOCKET_SERVER_ERROR(1500, "소켓 서버 오류입니다.");

    private final int code;
    private final String message;
}
