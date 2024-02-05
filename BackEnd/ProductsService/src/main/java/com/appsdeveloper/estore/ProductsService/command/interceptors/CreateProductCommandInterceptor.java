package com.appsdeveloper.estore.ProductsService.command.interceptors;

import com.appsdeveloper.estore.ProductsService.command.CreateProductCommand;
import com.appsdeveloper.estore.ProductsService.core.data.ProductLookupEntity;
import com.appsdeveloper.estore.ProductsService.core.data.ProductLookupRepository;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

@Component
public class CreateProductCommandInterceptor  implements MessageDispatchInterceptor<CommandMessage<?>> {

    private ProductLookupRepository productLookupRepository;

    public CreateProductCommandInterceptor(ProductLookupRepository productLookupRepository) {
        this.productLookupRepository = productLookupRepository;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> list) {

        return (index, command) ->
        {

            LOGGER.info("Intercepted command: " + command.getPayloadType());

            if(CreateProductCommand.class.equals(command.getPayloadType()))
            {
                CreateProductCommand createProductCommand = (CreateProductCommand)command.getPayload();
                ProductLookupEntity productLookupEntity = productLookupRepository
                        .findByProductIdOrTitle(createProductCommand.getProductId(),createProductCommand.getTitle());
                if(productLookupEntity != null)
                {
                    throw new IllegalStateException(
                            String.format("Product with id:%s or title:%s already exists",
                                    productLookupEntity.getProductId(),productLookupEntity.getTitle())
                    );
                }
            }
            return command;
        };
    }
}
