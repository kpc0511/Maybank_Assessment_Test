package com.maybank.platform.services.restapi.payload.response;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SocketMessageResponse implements Serializable {
    private String action;
    private JSONObject jsonObject;
}
