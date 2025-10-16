package com.maybank.platform.services.restapi.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.maybank.platform.services.util.LongToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    @JsonSerialize(using = LongToStringSerializer.class)
    private Long id;

    private String accountNumber;

    private BigDecimal trxAmount;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate trxDate;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime trxTime;

    private Long customerId;

    private Integer version;
}
