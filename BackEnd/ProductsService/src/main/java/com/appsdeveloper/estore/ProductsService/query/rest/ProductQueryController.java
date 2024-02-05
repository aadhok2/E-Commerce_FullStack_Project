package com.appsdeveloper.estore.ProductsService.query.rest;


import com.appsdeveloper.estore.ProductsService.core.data.ProductEntity;
import com.appsdeveloper.estore.ProductsService.query.FindProductByIdQuery;
import com.appsdeveloper.estore.ProductsService.query.FindProductsQuery;
import com.appsdeveloper.estore.ProductsService.query.ProductRestModel;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductQueryController {

    @Autowired
    QueryGateway queryGateway;

    private Logger LOGGER = LoggerFactory.getLogger(ProductQueryController.class);

    @GetMapping
    public List<ProductRestModel> getProducts(){

        FindProductsQuery findProductsQuery = new FindProductsQuery();
        List<ProductRestModel> prodcutRestModels = queryGateway.query(findProductsQuery,
                ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();

        return prodcutRestModels;

    }

    @GetMapping("/product/{productId}")
    public ProductRestModel getProduct(@PathVariable String productId) {
        LOGGER.info("Received productId: " + productId.getClass());

        FindProductByIdQuery findProductByIdQuery = new FindProductByIdQuery(productId);
        LOGGER.info("Query productId: " + findProductByIdQuery.getProductId());

        ProductRestModel productRestModel = queryGateway.query(findProductByIdQuery,
                ResponseTypes.instanceOf(ProductRestModel.class)).join();

        if (productRestModel != null) {
            LOGGER.info("Found product: " + productRestModel.getGalleryName());
        } else {
            LOGGER.warn("Product not found for productId: " + productId);
        }

        return productRestModel;
    }


}
