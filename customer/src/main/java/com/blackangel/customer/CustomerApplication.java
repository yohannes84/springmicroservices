package com.blackangel.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(
        scanBasePackages = {
                "com.blackangel.customer",
                "com.blackangel.advancedMQprotocol"
        }
)
@EnableEurekaClient
@EnableFeignClients(
    basePackages = "com.blackangel.clients"
)
@PropertySources({
        @PropertySource("classpath:clients-${spring.profiles.active}.yml")
})
public class CustomerApplication {
    public static void main(String[] args) {

        SpringApplication.run(CustomerApplication.class,args);

    }

}
