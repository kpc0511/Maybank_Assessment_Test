package com.maybank.platform.services.util.enums;

import java.util.HashMap;
import java.util.Map;

public enum ModeType {
    THIS_MONTH("thisMonth", 0),
    LAST_MONTH("lastMonth", 1),
    LAST_WEEK("lastWeek", 2),
    LAST_TWO_WEEK("lastTwoWeek", 3),
    LAST_HOUR("lastHour", 4),
    TIME("Time", 5),
    DATE("Date", 6),
    WEEK("Week", 7),
    MONTH("Month", 8),
    QUARTER("Quarter", 9),
    YEAR("Year", 10),
    ;

    private static final Map<String, ModeType> BY_LABEL = new HashMap<>();
    private static final Map<Integer, ModeType> BY_MODE = new HashMap<>();
    public final String label;
    public final Integer mode;

    static {
        for (ModeType e : values()) {
            BY_LABEL.put(e.label, e);
            BY_MODE.put(e.mode, e);
        }
    }

    ModeType(String label, Integer mode) {
        this.label = label;
        this.mode = mode;
    }

    public static ModeType valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }

    public static ModeType valueOfMode(Integer number) {
        return BY_MODE.get(number);
    }

    public Integer getMode() {
        return mode;
    }

    public String getLabel() {
        return label;
    }
}
