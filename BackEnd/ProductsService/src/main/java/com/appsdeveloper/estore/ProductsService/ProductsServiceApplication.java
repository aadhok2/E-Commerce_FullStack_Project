package com.appsdeveloper.estore.ProductsService;

import com.appsdeveloper.estore.Core.config.AxonXstreamConfig;
import com.appsdeveloper.estore.ProductsService.command.interceptors.CreateProductCommandInterceptor;
import com.appsdeveloper.estore.ProductsService.core.errorhandling.ProductsServiceEventsErrorHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;


@EnableDiscoveryClient
@SpringBootApplication
@Import({ AxonXstreamConfig.class })
public class ProductsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsServiceApplication.class, args);
	}

	@Autowired
	public void registerCreateProductCommandInterceptor(ApplicationContext context,
														CommandBus commandBus){
		commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
	}

	//Register the ListenerInvocationErrorHandler
	@Autowired
	public void configure(EventProcessingConfigurer config){

		config.registerListenerInvocationErrorHandler(
				"product-group",
				conf -> new ProductsServiceEventsErrorHandler()
		);
	}

	@Bean(name="productSnapshotTriggerDefinition")
	public SnapshotTriggerDefinition productSnapshotTriggerDefinition(Snapshotter snapshotter) {
		return new EventCountSnapshotTriggerDefinition(snapshotter, 3);
	}



}
