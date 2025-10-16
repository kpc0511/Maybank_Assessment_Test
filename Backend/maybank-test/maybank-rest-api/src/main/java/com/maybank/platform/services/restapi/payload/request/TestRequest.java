package com.maybank.platform.services.restapi.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestRequest {
    private Integer id;
    private String testName;
    private Integer status;
    @NotNull(message = "Please enter page number")
    private int page = 0;
    @NotNull(message = "Please enter page size")
    private int size = 1;
    private String[] sort = new String[]{"id,desc"};
}
