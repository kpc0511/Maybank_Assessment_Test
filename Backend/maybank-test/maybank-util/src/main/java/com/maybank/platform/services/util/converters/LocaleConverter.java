package com.maybank.platform.services.util.converters;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocaleConverter {
    private static final Logger log = LoggerFactory.getLogger(LocaleConverter.class);

    private LocaleConverter() {
    }

    public static Locale convertStringToLocale(String localeStr) {
        if (StringUtils.isBlank(localeStr)) {
            return null;
        } else {
            Locale locale = null;

            try {
                locale = LocaleUtils.toLocale(localeStr);
            } catch (Exception var3) {
                log.error("Error while convert to Locale", var3);
            }

            return locale;
        }
    }

    public static List<Locale> convertStringToLocaleList(String localeListStr, String del) {
        return (List)(StringUtils.isBlank(localeListStr) ? new ArrayList(0) : (List)
                Stream.of(localeListStr.split(del)).map((localeStr) -> {
            try {
                return LocaleUtils.toLocale(localeStr);
            } catch (Exception var2) {
                log.error("Error while convert to Locale", var2);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList()));
    }
}
