package com.maybank.platform.services.util;

import com.maybank.platform.services.util.constants.GlobalConstants;

import java.text.MessageFormat;

public class RedisDbKeyUtils {
    private RedisDbKeyUtils() {
    }

    public static String getRedisCacheRestApiKey(String name) {
        return MessageFormat.format(GlobalConstants.REST_API, name);
    }

    public static String getRedisCacheRestApiTokenKey(String token) {
        return MessageFormat.format(GlobalConstants.REST_API_TOKEN, token);
    }

    public static String getRedisCacheTransactionKey(Long id) {
        return MessageFormat.format(GlobalConstants.REST_API_TRANSACTION, id);
    }
}
