package com.maybank.platform.services.restapi.query;

import lombok.Data;

@Data
public class PageQuery {
    private int index = 0;
    private int size = 100;
    private int limit;
    private int offset;

    public void calculatePage(){
        this.limit = index;
        this.offset = size;
    }
}
