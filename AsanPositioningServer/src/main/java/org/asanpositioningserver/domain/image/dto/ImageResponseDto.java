package org.asanpositioningserver.domain.image.dto;

import lombok.AccessLevel;
import lombok.Builder;
import org.asanpositioningserver.domain.image.entity.Image;
import org.springframework.web.multipart.MultipartFile;

@Builder(access = AccessLevel.PRIVATE)
public record ImageResponseDto(
        Long imageId,
        String  imageUrl
) {
    public static ImageResponseDto of(Long imageId,  String  imageUrl) {
        return ImageResponseDto.builder()
                .imageId(imageId)
                .imageUrl(imageUrl)
                .build();
    }
}
