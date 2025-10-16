package com.maybank.platform.services.restapi.helper;

import com.maybank.platform.services.restapi.services.AccountService;
import com.maybank.platform.services.util.enums.FileType;

public class ParserFactory {
    public static Parser getParser(String fileType, AccountService accountService) {
        if (FileType.CSV.getKey().equalsIgnoreCase(fileType)) {
            return new CSVParser(accountService);
        } else if (FileType.TXT.getKey().equalsIgnoreCase(fileType)) {
            return new TXTParser(accountService);
        }
        throw new IllegalArgumentException("Unsupported file type: " + fileType);
    }
}
