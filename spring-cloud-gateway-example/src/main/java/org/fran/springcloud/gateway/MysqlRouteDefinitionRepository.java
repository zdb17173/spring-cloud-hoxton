package org.fran.springcloud.gateway;

import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MysqlRouteDefinitionRepository implements RouteDefinitionRepository {

    private static int order = 0;
    private static Map<String, RouteDefinition> routes = new LinkedHashMap<String, RouteDefinition>();

    public static void addRouteDefinition(String id,
                                                String uri,
                                                String[] predicates,
                                                String[] filters) throws URISyntaxException {
        RouteDefinition d = new RouteDefinition();
        d.setId(id);
        d.setUri(new URI(uri));
        if(filters!= null && filters.length > 0){
            List<FilterDefinition> lfd = new ArrayList<>();
            for(String f : filters){
               lfd.add(new FilterDefinition(f));
            }
            d.setFilters(lfd);
        }
        if(predicates!= null && predicates.length > 0){
            List<PredicateDefinition> lpd = new ArrayList<>();
            for(String f : predicates){
                lpd.add(new PredicateDefinition(f));
            }
            d.setPredicates(lpd);
        }
        d.setOrder(order);
        order ++;

        routes.put(id, d);
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {

        try {
            addRouteDefinition("path_route",
                    "https://www.baidu.com",
                    new String[]{"Path=/baidu/**"},
                    new String[]{"StripPrefix=1"});
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return Flux.fromIterable(routes.values());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {

        //todo: save to db
        return Mono.empty();
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        //todo: delete to db
        return Mono.empty();
    }
}