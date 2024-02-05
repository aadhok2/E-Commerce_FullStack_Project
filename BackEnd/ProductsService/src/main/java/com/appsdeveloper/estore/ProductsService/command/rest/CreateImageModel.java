package com.appsdeveloper.estore.ProductsService.command.rest;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
@Builder
public class CreateImageModel {
    private String id;
    private MultipartFile file;
}
