package com.maybank.platform.services.restapi.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TransactionRequest {
    private Long customerId;

    private List<String> accountNumbers;

    private String description;

    private int page = 0;

    private int size = 1;

    private String[] sort = new String[]{"id,desc"};
}
