package com.maybank.platform.services.restapi.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestQuery extends PageQuery {
    private Integer id;
    private String testName;
    private Integer status;
}
