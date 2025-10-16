package com.maybank.platform.services.restapi.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserListingRequest {
    private String displayName;
    @NotNull(message = "Please enter page number")
    private int page = 0;
    @NotNull(message = "Please enter page size")
    private int size = 1;
    private String[] sort = new String[]{"id,desc"};
}
