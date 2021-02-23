package org.fran.springcloud.gateway;

import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.cloud.gateway.route.InMemoryRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.RouteRefreshListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import java.net.URISyntaxException;

/**
 * @author fran
 * @Description
 * @Date 2020/3/13 11:40
 */
@RestController
public class DynamicAddRouteController {

    @Resource
    RouteRefreshListener routeRefreshListener;
    @Resource
    MysqlRouteDefinitionRepository mysqlRouteDefinitionRepository;

    @RequestMapping("/sss")
    public Result hello() {
        try {
            //动态注册sina到gateway
            mysqlRouteDefinitionRepository.addRouteDefinition(
                    "sina",
                    "https://www.sina.com.cn/",
                    new String[]{"Path=/sina/**"},
                    new String[]{"StripPrefix=1"}
            );

            //刷新所有路由表
            routeRefreshListener.onApplicationEvent(new RefreshScopeRefreshedEvent());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new Result(500, e.getMessage());
        }

        return new Result(200, "");
    }

    public static class Result{
        int status;
        String msg;

        public Result(int status, String msg) {
            this.status = status;
            this.msg = msg;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
