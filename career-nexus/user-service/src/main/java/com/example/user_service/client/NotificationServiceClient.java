package com.example.user_service.client;

import com.example.user_service.dto.SendNotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationServiceClient {
    @PostMapping("/api/notifications/send")
    void sendNotification(@RequestBody SendNotificationRequest request);
}