package org.asanpositioningserver.domain.position.controller;

import lombok.RequiredArgsConstructor;
import org.asanpositioningserver.domain.position.dto.request.PosDataDTO;
import org.asanpositioningserver.domain.position.dto.response.PositionResponseDto;
import org.asanpositioningserver.domain.position.service.PositionService;
import org.asanpositioningserver.socket.dto.MessageType;
import org.asanpositioningserver.socket.dto.SocketBaseResponse;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class PositionMessageController {
    private final PositionService positionService;
    private final SimpMessageSendingOperations sendingOperations;


    @MessageMapping("/position")
    public void sendPosition(@Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
                                  @Payload final PosDataDTO request) throws Exception {
        PositionResponseDto responseDto = positionService.receiveData(request);
        String destination = "/queue/sensor/" + responseDto.watchId();
        sendingOperations.convertAndSend(destination, SocketBaseResponse.of(MessageType.POSITION, responseDto));
    }


}
