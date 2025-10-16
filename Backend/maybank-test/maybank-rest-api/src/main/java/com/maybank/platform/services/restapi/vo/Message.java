package com.maybank.platform.services.restapi.vo;

import com.alibaba.fastjson2.JSONObject;
import com.maybank.platform.services.restapi.enums.MessageAction;
import com.maybank.platform.services.restapi.enums.MessageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    @Enumerated(EnumType.STRING)
    private MessageAction messageAction;
    private String token;
    private String message;
    private JSONObject jsonObject;
}
