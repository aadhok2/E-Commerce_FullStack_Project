package com.appsdeveloper.estore.ProductsService.query;

import com.appsdeveloper.estore.Core.events.ProductReservationCancelledEvent;
import com.appsdeveloper.estore.Core.events.ProductReservedEvent;
import com.appsdeveloper.estore.ProductsService.core.data.ProductEntity;
import com.appsdeveloper.estore.ProductsService.core.data.ProductsRepository;
import com.appsdeveloper.estore.ProductsService.core.events.ProductCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductEventsHandler {

    private final ProductsRepository productsRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(ProductEventsHandler.class);

    public ProductEventsHandler(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException ex){
        //Log error message
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception ex) throws Exception {
        //throw error message
        throw ex;
    }

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent){

        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(productCreatedEvent, productEntity);

        try{
            productsRepository.save(productEntity);
        }
        catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }
    @EventHandler
    public void on(ProductReservedEvent productReservedEvent) {

        try{
            ProductEntity productEntity = productsRepository.findByProductId(productReservedEvent.getProductId());

            LOGGER.debug("ProductReservedEvent here: Current product quantity " + productEntity.getQuantity());

            productEntity.setQuantity(productEntity.getQuantity() - productReservedEvent.getQuantity());


            productsRepository.save(productEntity);

            LOGGER.debug("ProductReservedEvent: New product quantity " + productEntity.getQuantity());

            LOGGER.info("ProductReservedEvent for productId:" + productReservedEvent.getProductId() +
                    " and orderId: " + productReservedEvent.getOrderId());
        }
        catch (Exception ex)
        {
            LOGGER.info(ex.getMessage());
        }

    }

    @EventHandler
    public void on(ProductReservationCancelledEvent productReservationCancelledEvent){
        try{
            LOGGER.info("Handling  Product reservation cancel event for product id: "+productReservationCancelledEvent.getProductId());
            ProductEntity currentlyStoredProduct =  productsRepository.findByProductId(productReservationCancelledEvent.getProductId());

            LOGGER.debug("ProductReservationCancelledEvent: Current product quantity "
                    + currentlyStoredProduct.getQuantity() );

            int newQuantity = currentlyStoredProduct.getQuantity() + productReservationCancelledEvent.getQuantity();
            currentlyStoredProduct.setQuantity(newQuantity);

            productsRepository.save(currentlyStoredProduct);

            LOGGER.debug("ProductReservationCancelledEvent: New product quantity "
                    + currentlyStoredProduct.getQuantity() );

        }
        catch (Exception ex)
        {
            LOGGER.info("In ProductEventsHandler: "+ex.getMessage());
        }
    }

//    @ResetHandler
//    public void reset() {
//        productsRepository.deleteAll();
//    }
}
