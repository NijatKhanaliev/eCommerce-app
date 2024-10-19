package com.company.demoshop.service.image;

import com.company.demoshop.dto.ImageDto;
import com.company.demoshop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> addImage(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file,Long imageId);
}
