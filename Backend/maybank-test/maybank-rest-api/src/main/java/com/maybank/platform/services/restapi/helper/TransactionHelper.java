package com.maybank.platform.services.restapi.helper;

import com.maybank.platform.services.restapi.model.Transaction;
import com.maybank.platform.services.restapi.services.AccountService;

import java.util.List;

public class TransactionHelper {
    private final AccountService accountService;

    public TransactionHelper(AccountService accountService) {
        this.accountService = accountService;
    }

    public List<Transaction> parseFile(String filePath, String fileType) {
        Parser parser = ParserFactory.getParser(fileType, accountService);
        return parser.parse(filePath);
    }
}
