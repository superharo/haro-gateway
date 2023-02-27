package online.superh.harogatewayali.filter.local;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2023-02-27 17:22
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 自定义局部过滤器(局部过滤器需要在路由中配置才能生效)
 * 名称必须是xxxGatewayFilterFactory形式
 * todo：模拟授权的验证，具体逻辑根据业务完善
 */
@Component
@Slf4j
public class AuthorizeGatewayFilterFactory {
}
