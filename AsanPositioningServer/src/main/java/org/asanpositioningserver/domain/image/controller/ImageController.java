package org.asanpositioningserver.domain.image.controller;


import lombok.RequiredArgsConstructor;
import org.asanpositioningserver.domain.image.dto.*;
import org.asanpositioningserver.domain.image.service.ImageService;
import org.asanpositioningserver.domain.position.dto.PositionDTO;
import org.asanpositioningserver.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/image")
@RestController
public class ImageController {
    private final ImageService imageService;

    //이미지 조회 api
    @GetMapping("/getImage/{id}")
    public ResponseEntity<SuccessResponse<?>> getImage(@PathVariable Long id) {
        ImageResponseDto responseDto = imageService.getImage(id);
        System.out.println("responseDto = " + responseDto);
        return SuccessResponse.ok(responseDto);
    }



    @GetMapping("/getImageList")
    public ResponseEntity<SuccessResponse<?>> getImageList()  {
        ImageListDTO responseDto = imageService.getImageList();
        System.out.println("responseDto = " + responseDto.getImageIds());
        return SuccessResponse.ok(responseDto);
    }



    //이미지 저장 api
    @PostMapping("/saveImage")
    public ResponseEntity<SuccessResponse<?>> saveImage(@RequestParam("imageData") MultipartFile file) throws IOException {
        Long imageId = imageService.saveImage(file);
        System.out.println("imageId = " + imageId);
        return SuccessResponse.ok(imageId);
    }

    @PostMapping("/nameChange")
    public ResponseEntity<SuccessResponse<?>> nameChange(@RequestBody ImageIdAndNameDTO imageIdAndNameDTO ) {
        Long imageId = imageService.nameChange(imageIdAndNameDTO);
        return SuccessResponse.ok(imageId);
    }


    //이미지 삭제 api
    @DeleteMapping("/deleteImage/{imageId}")
    public ResponseEntity<SuccessResponse<?>> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return SuccessResponse.ok(null);
    }


    //이미지내에 설정한 위치들의 목록을 가져오는 api
    @GetMapping("/getPositionList")
    public ResponseEntity<SuccessResponse<?>> getPositionList() {
        List<PositionDTO> positionList = imageService.getPositionList();
        return SuccessResponse.ok(positionList);
    }


    //이미지내에 설정한 위치들과 해당 좌표 목록을 가져오는 api
    @GetMapping("/getPositionAndCoordinateList/{id}")
    public ResponseEntity<SuccessResponse<?>> getPositionAndCoordinateList(@PathVariable Long id) {
        List<CoordinateDTO> positionList = imageService.getPositionAndCoordinateList(id);
        System.out.println("positionList = " + positionList);
        return SuccessResponse.ok(positionList);
    }


    //이미지 내 위치 및 범위 생성 api
    @PostMapping("/postImagePositionAndCoordinates")
    public ResponseEntity<SuccessResponse<?>> saveImagePositionAndCoordinates(@RequestBody LabelDataDTO labelDataDTO) {
        imageService.saveImagePositionAndCoordinates(labelDataDTO);
        return SuccessResponse.ok(null);

    }

    //이미지 내 위치 및 범위 삭제 api
    @DeleteMapping("/deleteImagePositionAndCoordinates/{positionName}")
    public ResponseEntity<SuccessResponse<?>> deleteImagePositionAndCoordinates(@PathVariable String positionName) {
        imageService.deleteImagePositionAndCoordinates(positionName);
        return SuccessResponse.ok(null);
    }
}

