package org.asanpositioningserver.domain.position.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.asanpositioningserver.domain.position.dto.PositionDTO;
import org.asanpositioningserver.domain.position.dto.request.GetStateDTO;
import org.asanpositioningserver.domain.position.dto.request.PositionNameDTO;
import org.asanpositioningserver.domain.position.dto.request.StateDTO;
import org.asanpositioningserver.domain.position.service.PositionService;
import org.asanpositioningserver.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class PositionController {
    private final PositionService positionService;


    @GetMapping("/countBeacon")
    public ResponseEntity<SuccessResponse<?>> countBeacon() {
        positionService.countBeacon();
        return SuccessResponse.ok(positionService.countBeacon());
    }

    @PostMapping("/createCsv")
    public ResponseEntity<SuccessResponse<?>> createCsv() throws JsonProcessingException {
        positionService.createCsv();
        return SuccessResponse.ok("success");
    }

    @DeleteMapping("/deleteBeacon")
    public ResponseEntity<SuccessResponse<?>> deleteBeacon(@RequestBody PositionNameDTO positionNameDTO) {
        positionService.deleteBeacon(positionNameDTO.getPosition());
        return SuccessResponse.ok("success");
    }

    @PostMapping("/insertState")
    public ResponseEntity<SuccessResponse<?>> insertState(@RequestBody StateDTO stateDTO) {
        positionService.insertState(stateDTO);
        return SuccessResponse.ok("success");
    }

    @DeleteMapping("/deleteState")
    public ResponseEntity<SuccessResponse<?>> deleteState(@RequestBody StateDTO stateDTO) {
        positionService.deleteState(stateDTO);
        return SuccessResponse.ok("success");
    }

    @GetMapping("/getCollectionStatus/{id}")
    public ResponseEntity<SuccessResponse<?>> getCollectionStatus(@PathVariable Long id){
        return SuccessResponse.ok(positionService.getCollectionState(id));
    }
}



