package com.maybank.platform.services.restapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    private final int POOL_SIZE = 100;

    @Bean
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(POOL_SIZE);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
//        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
//        threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
//        threadPoolTaskScheduler.setThreadNamePrefix("ev-scheduled-task-pool-");
//        threadPoolTaskScheduler.initialize();
//
//        scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
//    }
}
