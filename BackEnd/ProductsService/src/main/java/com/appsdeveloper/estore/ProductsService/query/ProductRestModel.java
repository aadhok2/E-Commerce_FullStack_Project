package com.appsdeveloper.estore.ProductsService.query;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
public class ProductRestModel {

    private String productId;
    private String title;
    private String galleryName;
    private BigDecimal price;
    private int quantity;
    private String imageId;
    private Date createdOn;
    private String description;
}
