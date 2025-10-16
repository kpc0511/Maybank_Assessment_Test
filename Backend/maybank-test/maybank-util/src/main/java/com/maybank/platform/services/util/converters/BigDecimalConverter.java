package com.maybank.platform.services.util.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvConstraintViolationException;

import java.math.BigDecimal;
public class BigDecimalConverter extends AbstractBeanField<BigDecimal, String> {

    @Override
    protected BigDecimal convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        try {
            if (value == null || value.isEmpty() || value.equals("#")) {
                // Return BigDecimal.ZERO when the value is "#" or empty/null
                return BigDecimal.ZERO;
            } else {
                // Parse the value as a BigDecimal
                return new BigDecimal(value.replace(",", ""));
            }
        } catch (NumberFormatException e) {
            throw new CsvDataTypeMismatchException(value, BigDecimal.class, "Conversion to BigDecimal failed.");
        }
    }
}
