package com.example.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Cách 2: Code Filter bằng Java triển khai GlobalFilter và Ordered để can thiệp vào Response
 */
@Component
public class ResponseHeaderFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(ResponseHeaderFilter.class);
    public static final String HEADER_NAME = "X-System-Name";
    public static final String HEADER_VALUE = "Api-Gateway-System";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().set(HEADER_NAME, HEADER_VALUE);
            log.info("ResponseHeaderFilter applied header [{} : {}]", HEADER_NAME, HEADER_VALUE);
        }));
    }

    @Override
    public int getOrder() {
        // Thứ tự ưu tiên thực thi của Filter (chạy sau khi Service đích trả về response)
        return Ordered.LOWEST_PRECEDENCE;
    }
}
