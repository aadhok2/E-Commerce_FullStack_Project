//package com.appsdeveloper.estore.ApiGateway.logging;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.io.IOException;
//
//@Component
//public class RequestLoggingFilter implements GlobalFilter, Ordered {
//
//    private final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        logger.info("Incoming request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());
//
//        // Continue with the filter chain
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        // Set the order of your filter
//        return Ordered.HIGHEST_PRECEDENCE;
//    }
//}