package org.asanpositioningserver.domain.image.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ImageListDTO {
    private List<Long> imageIds;
    private List<String> imageNames;
}
