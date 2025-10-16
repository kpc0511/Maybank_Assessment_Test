package com.maybank.platform.services.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class CustomLocale {
    private final ApplicationContext applicationContext;

    public CustomLocale(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    private void postConstruct() {
        if (Objects.nonNull(applicationContext)
                && Objects.nonNull(applicationContext.getBean(ResourceBundleMessageSource.class))) {
            applicationContext.getBean(ResourceBundleMessageSource.class).addBasenames("messages-custom");
        }
    }
}
