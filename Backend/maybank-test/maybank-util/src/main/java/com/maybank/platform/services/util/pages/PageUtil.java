package com.maybank.platform.services.util.pages;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageUtil {
    private PageUtil(){}

    public static <T> Page<T> paginateList(final Pageable pageable, List<T> list) {
        int totalpages = list.size() / pageable.getPageSize();
        //int first = Math.min(new Long(pageable.getOffset()).intValue(), list.size());;
        //int last = Math.min(first + pageable.getPageSize(), list.size());
        int last = pageable.getPageNumber()>=totalpages? list.size():pageable.getPageSize()*(pageable.getPageNumber()+1);
        int first = pageable.getPageNumber() >totalpages? last : pageable.getPageSize()*pageable.getPageNumber();
        return new PageImpl<>(list.subList(first, last), pageable, list.size());
    }
}
