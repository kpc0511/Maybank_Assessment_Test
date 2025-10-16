package com.maybank.platform.services.restapi.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

public class LocaleMsgUtils {
    private static final Logger log = LoggerFactory.getLogger(LocaleMsgUtils.class);
    private static ResourceBundleMessageSource messageSource;

    public static String getMsg(String msgCode, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return getMsg(msgCode, locale, args);
    }

    public static String getMsg(String msgCode, Locale locale, Object... args) {
        if (messageSource == null) {
            log.debug("messageSource is null");
            return msgCode;
        } else {
            return messageSource.getMessage(msgCode, args, locale);
        }
    }

    private LocaleMsgUtils() {
    }

    public static void setMessageSource(final ResourceBundleMessageSource messageSource) {
        LocaleMsgUtils.messageSource = messageSource;
    }
}
