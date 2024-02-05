package com.appsdeveloper.estore.ProductsService.core.data;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

// ImageEntity.java
@Data
@Document(collection = "products-images")
public class ImageEntity {

    @Id
    @Field(targetType = FieldType.STRING, name = "_id")
    private String id;
    private String imageName;
    private String contentType;
    private Binary image;

    // Constructors, getters, and setters
}

