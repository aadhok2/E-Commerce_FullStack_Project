package com.appsdeveloper.estore.ProductsService.core.data;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="products")
@Data
public class ProductEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -227264951080660124L;

    @Id
    @Column(unique=true)
    private String productId;

    @Column(unique=true)
    private String title;
    private BigDecimal price;
    private Integer quantity;
    private String imageId;
    private String galleryName;
    @CreationTimestamp
    private Date createdOn;
    @Column(columnDefinition = "TEXT")
    private String description;

}
