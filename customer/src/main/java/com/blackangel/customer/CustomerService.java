package com.blackangel.customer;

import com.blackangel.advancedMQprotocol.RabbitMQMessageProducer;
import com.blackangel.clients.fraud.FraudCheckResponse;
import com.blackangel.clients.fraud.FraudClient;
import com.blackangel.clients.notification.NotificationClient;
import com.blackangel.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    //private final RestTemplate restTemplate;
    private final FraudClient fraudClient;
    //private final NotificationClient notificationClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    public CustomerService(CustomerRepository customerRepository, FraudClient fraudClient, RabbitMQMessageProducer rabbitMQMessageProducer) {
        this.customerRepository = customerRepository;
        this.fraudClient = fraudClient;
        this.rabbitMQMessageProducer = rabbitMQMessageProducer;
    }

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email((request.email()))
                .build();
        //todo:check if email is valid
        //todo: check if email is not already taken
        customerRepository.saveAndFlush(customer);
        //todo: check if customer is fraudlent
//        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(---main code line
//                //this is used to make communication with the fraud services using network/
//                //api call , the idea to change this with something services discovery using eureka server
//                // this help to avoid the prior knowledge services required to know about the port numbers
//                //"http://localhost:4001/api/v1/fraud-check/{customerId}",----main code line
//                //Replace the port number with the service name directly
//                //........................................................................................
//                // and the next alternative of avoiding using the rest template is using OpenFeign
//                //helps to replace this in its entirity, better alteranative implementation strategy
//                // will follow up below
//                "http://FRAUD/api/v1/fraud-check/{customerId}",--main code line
//                FraudCheckResponse.class,---main code line
//                customer.getId()--main code line
//        );--main code line

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if(fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("fraudster");
        }else{

            NotificationRequest notificationRequest = new NotificationRequest(
                    customer.getId(), customer.getEmail(),
                    String.format("Hi %s, Welcome to BlackAngel...", customer.getFirstName()));
            //this below line replaced by a message producer and consumer model using rabbitMQ
            //this is because the notification service ca fail anytime or delayed
            //in this case the message queue will solve the problem
            //notificationClient.sendNotification(notificationRequest);
            this.rabbitMQMessageProducer.publish(
                    notificationRequest,
                    "internal.exchange",
                    "internal.notification.routing-key"
            );
        }

    }
}
