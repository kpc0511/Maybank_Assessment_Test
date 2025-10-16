package com.maybank.platform.services.util.enums;

import java.util.Arrays;
import java.util.Optional;

public enum EFileStatus {
    PENDING("PENDING","Pending"),
    COMPLETE("COMPLETE","Complete");

    private final String key;
    private final String desc;

    EFileStatus(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    public static Optional<EFileStatus> getFileStatus(String value) {
        return Arrays.stream(EFileStatus.values())
                .filter(e -> e.key.equalsIgnoreCase(value.replaceAll("\\s+","")))
                .findFirst();
    }
}
