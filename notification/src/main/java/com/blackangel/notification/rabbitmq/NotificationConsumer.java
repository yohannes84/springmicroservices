package com.blackangel.notification.rabbitmq;

import com.blackangel.clients.notification.NotificationRequest;
import com.blackangel.notification.NotificationServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationServices notificationServices;
    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void consumer(NotificationRequest notificationRequest){
        log.info("Consumed {} from queue", notificationRequest);
        notificationServices.send(notificationRequest);
    }
}
