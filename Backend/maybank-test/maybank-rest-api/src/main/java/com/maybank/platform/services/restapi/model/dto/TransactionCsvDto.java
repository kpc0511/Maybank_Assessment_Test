package com.maybank.platform.services.restapi.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TransactionCsvDto {
    private String accountNumber;
    private BigDecimal trxAmount;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate trxDate;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime trxTime;
    private Long customerId;
}
