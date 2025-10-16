package com.maybank.platform.services.restapi.services;

import io.github.bucket4j.Bucket;

public interface RateLimitPricePlanService {
    Bucket getPlanServiceBucket(String clientToken);
}
