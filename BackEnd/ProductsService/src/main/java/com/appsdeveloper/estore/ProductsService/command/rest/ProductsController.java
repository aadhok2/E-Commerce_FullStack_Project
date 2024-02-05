package com.appsdeveloper.estore.ProductsService.command.rest;

import com.appsdeveloper.estore.ProductsService.command.CreateProductCommand;
import com.appsdeveloper.estore.ProductsService.core.data.ImageEntity;
import com.appsdeveloper.estore.ProductsService.core.data.ProductEntity;
import com.appsdeveloper.estore.ProductsService.core.data.ProductsRepository;
import com.appsdeveloper.estore.ProductsService.service.ImageService;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductsController{


    private final Environment env;
    private  final CommandGateway commandGateway;

    private final ImageService imageService;

    private final ProductsRepository productsRepository;

    private Logger LOGGER = LoggerFactory.getLogger(ProductsController.class);

    public ProductsController(Environment env, CommandGateway commandGateway, ImageService imageService,
                              ProductsRepository productsRepository) {
        this.env = env;
        this.commandGateway = commandGateway;
        this.imageService = imageService;
        this.productsRepository = productsRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@Valid @ModelAttribute CreateProductRestModel createProductRestModel
    ,@RequestParam("file") MultipartFile multipartfile)
    {
        LOGGER.info("here "+createProductRestModel.getTitle());
        // Create a LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.now();

        // Convert LocalDateTime to Date
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        Date date = Date.from(zonedDateTime.toInstant());
        LOGGER.info("created on: " + date);

        String created_productId = UUID.randomUUID().toString();
        String created_imageId = UUID.randomUUID().toString();
        CreateProductCommand command = CreateProductCommand.builder()
                .productId(created_productId)
                .price(createProductRestModel.getPrice())
                .title(createProductRestModel.getTitle())
                .galleryName(createProductRestModel.getGalleryName())
                .quantity(createProductRestModel.getQuantity())
                .imageId(created_imageId)
                .createdOn(date)
                .description(createProductRestModel.getDescription())
                .build();
        String product_create_returnValue;
        product_create_returnValue = commandGateway.sendAndWait(command);
        try {
            CreateImageModel createImageModel = CreateImageModel.builder()
                    .id(created_imageId)
                    .file(multipartfile)
                    .build();
            ImageEntity savedImage = imageService.saveImage(createImageModel);
            return new ResponseEntity<>("Image create response: Image uploaded successfully. productID: "
                    + created_productId
                    + "Product create response: " + product_create_returnValue,HttpStatus.OK);
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
            return new ResponseEntity<>("Image create response: Failed to upload image with productId: "
                    + created_productId
                    + "Product create response: " + product_create_returnValue,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/images/{imageId}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageId) {
        try {
            // Fetch the image as a Resource from the service
            Resource imageResource = imageService.loadImageAsResource(imageId);

            // Return the image as ResponseEntity
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Adjust content type based on your image type
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageResource.getFilename() + "\"")
                    .body(imageResource);
        } catch (IOException e) {
            // Handle exceptions
            LOGGER.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
