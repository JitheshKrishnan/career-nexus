package com.example.user_service.dto;

import lombok.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendNotificationRequest {
    private Long userId;
    private String type;
    private List<String> channels;
    private String priority;
    private String templateName;
    private Map<String, Object> data;
}