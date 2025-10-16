package com.maybank.platform.services.restapi.vo;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CSVData {
    // Column 1: ACCOUNT_NUMBER
    @CsvBindByPosition(position = 0)
    private String accountNumber;

    // Column 2: TRX_AMOUNT
    @CsvBindByPosition(position = 1)
    private double trxAmount;

    // Column 3: DESCRIPTION
    @CsvBindByPosition(position = 2)
    private String description;

    // Column 4: TRX_DATE
    @CsvBindByPosition(position = 3)
    private String trxDate;

    // Column 5: TRX_TIME
    @CsvBindByPosition(position = 4)
    private String trxTime;

    // Column 6: CUSTOMER_ID
    @CsvBindByPosition(position = 5)
    private String customerId;
}
