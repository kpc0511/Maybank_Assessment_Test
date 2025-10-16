package com.maybank.platform.services.restapi.config;

import com.maybank.platform.services.restapi.common.LocaleMsgUtils;
import com.maybank.platform.services.util.converters.LocaleConverter;
import feign.RequestInterceptor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
public class LocaleConfig extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {
    @Value("${internationalization.enable:false}")
    private boolean isEnable = false;
    @Value("${internationalization.default-locale:}")
    private String defaultLocaleStr = "";
    @Value("${internationalization.supported-locales:}")
    private String supportedLocaleStr = "";

    public LocaleConfig() {
    }

    @Bean
    public LocaleResolver localeResolver() {
        if (this.isEnable) {
            Locale defaultLocale = LocaleConverter.convertStringToLocale(this.defaultLocaleStr);
            if (defaultLocale == null) {
                defaultLocale = Locale.US;
            }

            AcceptHeaderLocaleResolver ahlr = new AcceptHeaderLocaleResolver();
            ahlr.setDefaultLocale(defaultLocale);
            Locale.setDefault(defaultLocale);
            List<Locale> supportedLocale = LocaleConverter.convertStringToLocaleList(this.supportedLocaleStr, ",");
            if (CollectionUtils.isNotEmpty(supportedLocale)) {
                ahlr.setSupportedLocales(supportedLocale);
            }
            return ahlr;
        } else {
            FixedLocaleResolver flr = new FixedLocaleResolver();
            flr.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
            Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
            return flr;
        }
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
        rs.setBasenames(new String[]{"messages", "common-messages"});
        rs.setDefaultEncoding("UTF-8");
        rs.setUseCodeAsDefaultMessage(true);
        LocaleMsgUtils.setMessageSource(rs);
        return rs;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return (requestTemplate) -> {
            requestTemplate.header("Accept-Language", new String[]{LocaleContextHolder.getLocale().toLanguageTag()});
        };
    }

    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(this.messageSource());
        return validator;
    }
}
