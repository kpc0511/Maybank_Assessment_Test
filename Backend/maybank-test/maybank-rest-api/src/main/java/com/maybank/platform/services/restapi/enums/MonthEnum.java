package com.maybank.platform.services.restapi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum MonthEnum {
    JAN(1, "Jan"),
    FEB(2, "Feb"),
    MAR(3, "Mar"),
    APR(4, "Apr"),
    MAY(5, "May"),
    JUN(6, "Jun"),
    JUL(7, "Jul"),
    AUG(8, "Aug"),
    SEP(9, "Sep"),
    OCT(10, "Oct"),
    NOV(11, "Nov"),
    DEC(12, "Dec"),
    ;
    private final int code;

    private final String name;

    public static String getNameByCode(int code) {
        Optional<MonthEnum> groupType = Arrays.stream(MonthEnum.values()).filter(type -> type.getCode() == code).findAny();
        return groupType.map(Enum::name).orElse("");
    }
}
