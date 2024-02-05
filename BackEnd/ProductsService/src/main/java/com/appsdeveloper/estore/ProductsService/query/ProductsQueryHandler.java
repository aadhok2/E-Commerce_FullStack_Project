package com.appsdeveloper.estore.ProductsService.query;

import com.appsdeveloper.estore.ProductsService.core.data.ProductEntity;
import com.appsdeveloper.estore.ProductsService.core.data.ProductsRepository;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class ProductsQueryHandler {

    ProductsRepository productsRepository;

    Logger LOGGER = LoggerFactory.getLogger(ProductsQueryHandler.class);

    public ProductsQueryHandler(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @QueryHandler
    public List<ProductRestModel> findProducts(FindProductsQuery query){

        var productRestModels = new ArrayList<ProductRestModel>();
        var productEntities= productsRepository.findAll();
        for(var productEntity:productEntities){
            var productRestModel = new ProductRestModel();
            BeanUtils.copyProperties(productEntity,productRestModel);
            productRestModels.add(productRestModel);
        }
        return productRestModels;
    }

    @QueryHandler
    public ProductRestModel findProductByProductId(FindProductByIdQuery query)
    {
//        LOGGER.info(query.getProductId());
//        LOGGER.info(String.valueOf(query.getProductId().getClass()));
//        var temp = productsRepository.findByProductId(query.getProductId());
//        LOGGER.info(temp!=null
//        ? temp.getTitle():"null");
        var productRestModel = new ProductRestModel();
        try {
            ProductEntity productEntity = productsRepository.findByProductId(query.getProductId());
            productRestModel.setProductId(productEntity.getProductId());
            productRestModel.setQuantity(productEntity.getQuantity());
            productRestModel.setTitle(productEntity.getTitle());
            productRestModel.setPrice(productEntity.getPrice());
            productRestModel.setImageId(productEntity.getImageId());
            productRestModel.setGalleryName(productEntity.getGalleryName());
            productRestModel.setDescription(productEntity.getDescription());
            productRestModel.setCreatedOn(productEntity.getCreatedOn());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return productRestModel;
    }
}
