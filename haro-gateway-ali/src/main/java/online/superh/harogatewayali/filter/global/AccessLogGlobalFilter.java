package online.superh.harogatewayali.filter.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2023-02-27 17:23
 */

/*
 * GlobalFilter 的功能其实和 GatewayFilter 是相同的，只是 GlobalFilter 的作用域是所有的路由配置，而不是绑定在指定的路由配置上。
 * 多个 GlobalFilter 可以通过 @Order 或者 getOrder() 方法指定执行顺序，order值越小，执行的优先级越高。
 * 注意，由于过滤器有 pre 和 post 两种类型，pre 类型过滤器如果 order 值越小，
 * 那么它就应该在pre过滤器链的顶层，post 类型过滤器如果 order 值越小，那么它就应该在 post 过滤器链的底层。
 */
@Slf4j
@Component
@Order(value = Integer.MIN_VALUE)
public class AccessLogGlobalFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //filter的前置处理
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().pathWithinApplication().value();
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        return chain
                //继续调用filter
                .filter(exchange)
                //filter的后置处理
                .then(Mono.fromRunnable(() -> {
                    ServerHttpResponse response = exchange.getResponse();
                    HttpStatus statusCode = response.getStatusCode();
                    log.info("请求路径:{},远程IP地址:{},响应码:{}", path, remoteAddress, statusCode);
                }));
    }
}
