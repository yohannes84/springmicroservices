package com.blackangel.notification;

import com.blackangel.advancedMQprotocol.RabbitMQMessageProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(
        scanBasePackages = {
                "com.blackangel.notification",
                "com.blackangel.advancedMQprotocol" })
public class NotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class,args);
    }

    @Bean
    CommandLineRunner commandLineRunner (RabbitMQMessageProducer rabbitMQMessageProducer,
                                         NotificationConfig notificationConfig){
        return args -> {
            rabbitMQMessageProducer.publish(new Person("Yohannes", 35),
                    notificationConfig.getInternalExchange(),
                    notificationConfig.getInternalNotificationRoutingKey());
        };
    }


    record Person(String name,Integer age){}
}
