package online.superh.harogatewayali.filter.local;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2023-02-27 17:22
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义局部过滤器(局部过滤器需要在路由中配置才能生效)
 * 名称必须是xxxGatewayFilterFactory形式
 * todo：模拟授权的验证，具体逻辑根据业务完善
 */
@Component
@Slf4j
public class AuthorizeGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorizeGatewayFilterFactory.Config> {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Config {
        // 控制是否开启认证
        private boolean enabled;
    }

    //构造函数，加载Config
    public AuthorizeGatewayFilterFactory() {
        //固定写法
        super(AuthorizeGatewayFilterFactory.Config.class);
    }


    //读取配置文件中的参数 赋值到 配置类中
    @Override
    public List<String> shortcutFieldOrder() {
        return super.shortcutFieldOrder();
    }

    //认证
    @Override
    public GatewayFilter apply(Config config) {
        return null;
    }
}
