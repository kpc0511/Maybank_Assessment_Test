package com.maybank.platform.services.restapi.payload.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionUpdateRequest {
    private String description;

    private Integer version;
}
