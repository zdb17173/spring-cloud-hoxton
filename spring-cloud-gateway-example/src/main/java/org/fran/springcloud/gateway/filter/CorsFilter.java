package org.fran.springcloud.gateway.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import java.util.List;

/**
 * @author fran
 * @Description
 * @Date 2021/2/23 16:18
 */
@Configuration
public class CorsFilter implements WebFilter {
    private static final String ALL = "*";
    private static final String MAX_AGE = "18000L";
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        if (!CorsUtils.isCorsRequest(request)) {
            return webFilterChain.filter(serverWebExchange);
        }
        HttpHeaders requestHeaders = request.getHeaders();
        ServerHttpResponse response = serverWebExchange.getResponse();
        HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
        HttpHeaders headers = response.getHeaders();
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
        List<String> accessControlRequestHeaders = requestHeaders.getAccessControlRequestHeaders();
        if(accessControlRequestHeaders != null){
            //it seems only chrome support like this: Access-Control-Allow-Headers: content-type, x-auth-token
            //between headers, have blank, after ,
            //edge, firefox don't support this style, they only support like below:
            //Access-Control-Request-Headers: content-type,x-auth-token
            //so stupid and suck
            StringBuffer stringBuffer = new StringBuffer();
            accessControlRequestHeaders.forEach((header)->{
                if(stringBuffer.length() > 0){
                    stringBuffer.append("," + header);
                }else {
                    stringBuffer.append(header);
                }
            });
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, stringBuffer.toString());
        }
        if (requestMethod != null) {
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
        }
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, ALL);
        headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
        if (request.getMethod() == HttpMethod.OPTIONS) {
            response.setStatusCode(HttpStatus.OK);
            return Mono.empty();
        }
        return webFilterChain.filter(serverWebExchange);
    }
}
