package com.appsdeveloper.estore.ProductsService.core.data;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ImageRepository extends MongoRepository<ImageEntity, String> {
}
