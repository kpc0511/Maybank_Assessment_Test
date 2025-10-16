package com.maybank.platform.services.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

public class TestCustomLocale {
    @Test
    public void testGetCustomLocale(){
        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
        rs.setBasenames(new String[]{"messages-custom"});
        rs.setDefaultEncoding("UTF-8");
        rs.setUseCodeAsDefaultMessage(true);
        rs.setDefaultLocale(Locale.CHINESE);

        String translateMsgZH = rs.getMessage("custom.testing", null, Locale.CHINESE);
        String translateMsgEN = rs.getMessage("custom.testing", null, Locale.ENGLISH);
        String translateMsgMY = rs.getMessage("custom.testing", null, Locale.forLanguageTag("ms-MY"));
        String translateDefault = rs.getMessage("custom.testing", null, Locale.forLanguageTag("default-MY"));
        assertEquals("测试", translateMsgZH);
        assertEquals("testing", translateMsgEN);
        assertEquals("ujian", translateMsgMY);
        assertEquals("测试", translateDefault);
    }
}
