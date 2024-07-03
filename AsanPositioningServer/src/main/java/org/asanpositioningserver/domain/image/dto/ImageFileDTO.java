package org.asanpositioningserver.domain.image.dto;


import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ImageFileDTO {
    private MultipartFile imageData;
}
