package online.superh.order.ali.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2023-02-27 16:58
 */
@RestController
//自动刷新配置中心的值
@RefreshScope
public class TestConfigController {

    @Value("${haro}")
    private String haro;

    @Value("${server.port}")
    private String port;

    @GetMapping("/test")
    public String test(){
        return "haro:"+haro+"-----"+port;
    }

}
