package org.asanpositioningserver.domain.image.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.asanpositioningserver.domain.image.dto.*;
import org.asanpositioningserver.domain.image.entity.Coordinate;
import org.asanpositioningserver.domain.image.entity.Image;
import org.asanpositioningserver.domain.image.repository.CoordinateRepository;
import org.asanpositioningserver.domain.image.repository.ImageRepository;
import org.asanpositioningserver.domain.position.dto.PositionDTO;
import org.asanpositioningserver.domain.position.repository.BeaconDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class ImageService {
    private final CoordinateRepository coordinateRepository;
    private final ImageRepository imageRepository;
    private final BeaconDataRepository beaconDataRepository;
    public static String UPLOAD_DIR = "C:\\Users\\AMC-guest\\uploads\\images\\";
//    public static String UPLOAD_DIR = "/app/uploads/images/";

    public ImageResponseDto getImage(Long id)  {
        Optional<Image> image = imageRepository.findById(id);
        String imageUrl = image.get().getImageUrl();
        return  ImageResponseDto.of(image.get().getId(),imageUrl);
    }



    public ImageListDTO getImageList() {
        List<Image> images = imageRepository.findAll();
        List<Long>  imageIdDtoArrayList = new ArrayList<>();
        List<String>  imageNameDtoArrayList = new ArrayList<>();

        for (Image image : images) {
            imageIdDtoArrayList.add(image.getId());
            imageNameDtoArrayList.add(image.getImageName());

        }
        ImageListDTO imageListDTO = new ImageListDTO();
        imageListDTO.setImageIds(imageIdDtoArrayList);
        imageListDTO.setImageNames(imageNameDtoArrayList);
        return imageListDTO;
    }


    public Long saveImage(MultipartFile file) throws IOException {

        byte[] bytes = file.getBytes();
        Path path = Paths.get(UPLOAD_DIR   + file.getOriginalFilename());
        Files.write(path, bytes);

        Image image = Image.builder().imageUrl("/images/" + file.getOriginalFilename()).imageName("지정되지 않음").build();
        Image saveImage = imageRepository.save(image);
        return saveImage.getId();
    }

    public Long nameChange(ImageIdAndNameDTO imageIdAndNameDTO) {

        Image image = (imageRepository.findById(imageIdAndNameDTO.getImageId()).orElseThrow(() ->
                new IllegalArgumentException("해당 이미지가 존재하지 않습니다 :" + imageIdAndNameDTO.getImageId())));


        image.updateName(imageIdAndNameDTO.getImageName());

        return image.getId();
    }


    public void deleteImage(Long imageId) {

        Image image = (imageRepository.findById(imageId).orElseThrow(() ->
                new IllegalArgumentException("해당 이미지가 존재하지 않습니다 :" + imageId)));

        beaconDataRepository.deleteAllByImageId(image.getId());
        imageRepository.delete(image);

    }

    public void saveImagePositionAndCoordinates(LabelDataDTO labelDataDTO) {

        Image image = (imageRepository.findById(labelDataDTO.getImageId()).orElseThrow(() ->
                new IllegalArgumentException("해당 이미지가 존재하지 않습니다")));


        Coordinate existingCoordinate = coordinateRepository.findByImageIdAndPosition(image, labelDataDTO.getPosition());

        if (existingCoordinate != null) {
            throw new IllegalArgumentException("해당 이미지의 위치가 이미 존재합니다.");
        }
        System.out.println("labelDataDTO.getStartX() = " + labelDataDTO.getStartX());
        try {
            Coordinate coordinate = Coordinate.builder()
                    .imageId(image)
                    .position(labelDataDTO.getPosition())
                    .latitude(labelDataDTO.getLatitude())
                    .longitude(labelDataDTO.getLongitude())
                    .startX(labelDataDTO.getStartX())
                    .startY(labelDataDTO.getStartY())
                    .endX(labelDataDTO.getEndX())
                    .endY(labelDataDTO.getEndY()).build();

            coordinateRepository.save(coordinate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteImagePositionAndCoordinates(String positionName) {
        Coordinate coordinate = coordinateRepository.findByPosition(positionName);
        beaconDataRepository.deleteAllByPosition(positionName);
        coordinateRepository.delete(coordinate);
    }

    public List<CoordinateDTO> getPositionAndCoordinateList(Long id) {
        Optional<Image> image = imageRepository.findById(id);
        List<Coordinate> coordinateList = coordinateRepository.findAllByImageId(image);
        List<CoordinateDTO> coordinateDTOList = new ArrayList<>();

        if (coordinateList.isEmpty()) {
            throw new IllegalArgumentException("해당 이미지의 위치 목록이 존재하지 않습니다");
        } else {

            for (Coordinate coordinate : coordinateList) {
                CoordinateDTO coordinateDTO = new CoordinateDTO();
                coordinateDTO.setImageId(coordinate.getImageId().getId());
                coordinateDTO.setCoordinateId(coordinate.getId());
                coordinateDTO.setLatitude(coordinate.getLatitude());
                coordinateDTO.setLongitude(coordinate.getLongitude());
                coordinateDTO.setPosition(coordinate.getPosition());
                coordinateDTO.setStartX(coordinate.getStartX());
                coordinateDTO.setStartY(coordinate.getStartY());
                coordinateDTO.setEndX(coordinate.getEndX());
                coordinateDTO.setEndY(coordinate.getEndY());
                coordinateDTOList.add(coordinateDTO);
            }
        }

        return coordinateDTOList;
    }

    @Transactional
    public List<PositionDTO> getPositionList() {
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        List<PositionDTO> positionList = new ArrayList<>();

        if (coordinateList.isEmpty()) {
            throw new IllegalArgumentException("해당 이미지의 위치 목록이 존재하지 않습니다");
        } else {
            for (Coordinate coordinate : coordinateList) {
                PositionDTO positionDTO = new PositionDTO();
                positionDTO.setImageId(coordinate.getImageId().getId());
                positionDTO.setCoordinateId(coordinate.getId());
                positionDTO.setPosition(coordinate.getPosition());

                positionList.add(positionDTO);
            }
        }
        return positionList;
    }
}



