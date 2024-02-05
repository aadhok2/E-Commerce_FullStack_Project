package com.appsdeveloper.estore.ProductsService.service;

import com.appsdeveloper.estore.ProductsService.command.rest.CreateImageModel;
import com.appsdeveloper.estore.ProductsService.command.rest.CreateImageModel;
import com.appsdeveloper.estore.ProductsService.core.data.ImageEntity;
import com.appsdeveloper.estore.ProductsService.core.data.ImageRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

// ImageService.java
@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public ImageEntity saveImage(CreateImageModel createImageModel) throws IOException {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setId(createImageModel.getId());
        imageEntity.setImageName(createImageModel.getFile().getOriginalFilename());
        imageEntity.setContentType(createImageModel.getFile().getContentType());
        imageEntity.setImage(new Binary(BsonBinarySubType.BINARY, createImageModel.getFile().getBytes()));
        return imageRepository.save(imageEntity);
    }

    public ImageEntity getImageById(String  imageId) {
        return imageRepository.findById(imageId).get();
    }


    public Resource loadImageAsResource(String imageId) throws FileNotFoundException {
        try {
            Optional<ImageEntity> imageEntityOptional = imageRepository.findById(imageId);

            if (imageEntityOptional.isPresent()) {
                ImageEntity imageEntity = imageEntityOptional.get();
                byte[] imageBytes = imageEntity.getImage().getData();

                // Log the length of the imageBytes
                System.out.println("Image Bytes Length: " + imageBytes.length);

                // Return the ByteArrayResource
                return new ByteArrayResource(imageBytes);
            } else {
                // Handle the case where the image file is not found
                throw new FileNotFoundException("Image not found");
            }
        } catch (Exception e) {
            // Log the exception details for troubleshooting
//            e.printStackTrace();
            throw new FileNotFoundException("Error loading image: " + e.getMessage());
        }
    }


    // Add more methods as needed
}

