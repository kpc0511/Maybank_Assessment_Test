package com.maybank.platform.services.util.enums;

import lombok.Getter;

@Getter
public enum FileType {
    TXT("TXT"),
    CSV("CSV"),
    JSON("JSON"),
    ;
    private final String key;

    FileType(String key) {
        this.key = key;
    }
}
