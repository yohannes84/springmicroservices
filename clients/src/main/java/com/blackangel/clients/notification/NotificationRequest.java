package com.blackangel.clients.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class NotificationRequest {

        private Integer toCustomerId;
        private String toCustomerEmail;
        private String message;

}
