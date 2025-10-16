package com.maybank.platform.services.util;


import com.alibaba.fastjson2.JSONObject;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class QueryStringParser {
    public static JSONObject parse(String query) {
        Map<String, String> params = new HashMap<>();

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
            String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
            params.put(key, value);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(params);
        return jsonObject;
    }
}
