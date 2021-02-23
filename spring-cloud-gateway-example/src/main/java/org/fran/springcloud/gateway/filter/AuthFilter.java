package org.fran.springcloud.gateway.filter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author fran
 * @Description
 * @Date 2021/2/23 15:53
 */
@Configuration
public class AuthFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {

        ServerHttpRequest request = serverWebExchange.getRequest();
        String requestUri = request.getPath().pathWithinApplication().value();
        ServerHttpRequest.Builder mutate = request.mutate();
        ServerHttpRequest build = mutate.build();

        //TODO: handle ignore requestUri
        if (true) {
            return gatewayFilterChain.filter(serverWebExchange.mutate().request(build).build());
        }

        try{
            //TODO: check token
            List<String> authorization = request.getHeaders().get("JWT_USER_AUTH_TOKEN_HEADER");
            String authToken = null;
            if (authorization != null && authorization.size() > 0) {
                authToken = authorization.get(0);
                if(authToken == null || authToken.equals("")){
                    throw new Exception("token is null");
                }
            }else
                throw new Exception("token is null");


            //TODO: token getUser

            //TODO: check user permission

        }catch (Exception e){
            String msg = String.format("User Session Forbidden or Expired!:%s, please try to re-login", e.getMessage());
            return getVoidMono(serverWebExchange, "{\"status\":400, \"description\": \""+ msg +"\"}");
        }


        return gatewayFilterChain.filter(serverWebExchange.mutate().request(build).build());
    }


    private Mono<Void> getVoidMono(ServerWebExchange serverWebExchange, String bodyStr) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);

        byte[] bytes = bodyStr.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(bytes);
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }
}
