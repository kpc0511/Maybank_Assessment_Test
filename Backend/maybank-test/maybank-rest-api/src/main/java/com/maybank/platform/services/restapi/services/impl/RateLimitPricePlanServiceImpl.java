package com.maybank.platform.services.restapi.services.impl;

import com.maybank.platform.services.restapi.services.RateLimitPricePlanService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitPricePlanServiceImpl implements RateLimitPricePlanService {

    @Value("${rate.limit.client.basic}")
    private String basic;
    @Value("${rate.limit.client.free}")
    private String free;

    @Override
    public Bucket getPlanServiceBucket(String clientToken) {
        return Bucket4j.builder()
                .addLimit(getClientBandwidth(clientToken))
                .build();
    }

    private Bandwidth getClientBandwidth(String clientToken) {
        if(clientToken.equals(basic))
            return Bandwidth.classic(5, Refill.of(5, Duration.ofMinutes(1)));
        else if(clientToken.equals(free))
            return Bandwidth.classic(25, Refill.of(25, Duration.ofMinutes(1)));
        return Bandwidth.classic(2, Refill.of(2, Duration.ofMinutes(1)));
    }
}
