package com.maybank.platform.services.util.converters;

import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.bean.AbstractBeanField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter extends AbstractBeanField<Date, String> {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Override
    protected Date convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        try {
            return formatter.parse(value);
        } catch (ParseException e) {
            throw new CsvDataTypeMismatchException(value, Date.class, "Failed to parse Date: " + value);
        }
    }
}
