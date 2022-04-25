package com.buy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableRedisHttpSession(redisNamespace = "userInfo")
public class WebApp {
    public static void main(String[] args) {
        SpringApplication.run(WebApp.class,args);
    }
}
