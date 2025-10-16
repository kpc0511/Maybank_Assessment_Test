package com.maybank.platform.services.restapi.query;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {
    private Long totalPage;
    private Long totalCount;
    private List<T> datas;

    public Page(Long totalCount, List<T> datas) {
        this.totalCount = totalCount;
        this.datas = datas;
    }

    public Page(Long totalCount, List<T> datas, Long pageSize) {
        this.totalCount = totalCount;
        this.datas = datas;
        this.totalPage = totalCount / pageSize;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
