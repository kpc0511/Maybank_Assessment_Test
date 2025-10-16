package com.maybank.platform.services.util.constants;

public interface GlobalConstants {

    String GLOBAL_REDIS_KEY = "maybank:";

    String FILE_INFO_LOCK_KEY = GLOBAL_REDIS_KEY + "fileinfo:lock:";

    String REST_API = GLOBAL_REDIS_KEY + "rest:api:{0}";
    String REST_API_TRANSACTION = GLOBAL_REDIS_KEY + "rest:api:transaction:{0}";

    String REST_API_TOKEN = GLOBAL_REDIS_KEY + "rest:api:token:{0}";
}
