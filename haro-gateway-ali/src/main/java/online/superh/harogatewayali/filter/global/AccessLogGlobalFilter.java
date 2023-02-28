package online.superh.harogatewayali.filter.global;

import lombok.extern.slf4j.Slf4j;
import online.superh.harogatewayali.filter.GlobalFilterOrderEnum;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

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
public class AccessLogGlobalFilter implements GlobalFilter , Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("-----------------------AccessLogGlobalFilter-----------------------");
        // //filter的前置处理
        // ServerHttpRequest request = exchange.getRequest();
        // String path = request.getPath().pathWithinApplication().value();
        // InetSocketAddress remoteAddress = request.getRemoteAddress();
        // return chain
        //         //继续调用filter
        //         .filter(exchange)
        //         //filter的后置处理
        //         .then(Mono.fromRunnable(() -> {
        //             ServerHttpResponse response = exchange.getResponse();
        //             HttpStatus statusCode = response.getStatusCode();
        //             log.info("请求路径:{},远程IP地址:{},响应码:{}", path, remoteAddress, statusCode);
        //         }));
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                HttpHeaders headers=getHeaders();
                ContentDisposition contentDisposition=headers.getContentDisposition();
                // 为附件的响应头 不打印响应日志
                if (HttpStatus.OK.equals(getStatusCode()) && body instanceof Flux && !contentDisposition.isAttachment()) {
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBuffer join = dataBufferFactory.join(dataBuffers);
                        byte[] content = new byte[join.readableByteCount()];
                        join.read(content);
                        String responseData = new String(content, Charset.forName("UTF-8"));
                        log.info("***********************************响应信息**********************************");
                        log.info("响应内容:{}", responseData);
                        log.info("****************************************************************************\n");
                        DataBufferUtils.release(join);
                        //byte[] uppedContent = responseData.getBytes();
                        return bufferFactory.wrap(content);
                    }));
                } else if(!contentDisposition.isAttachment()){
                    //保存日志
                    log.error("响应code异常:{}", getStatusCode());
                }
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }
    @Override
    public int getOrder() {
        return GlobalFilterOrderEnum.ACCESS_LOG_GLOBALFILTER;
    }
}
