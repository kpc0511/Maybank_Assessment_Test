package com.maybank.platform.services.restapi.helper;

import com.maybank.platform.services.restapi.model.Transaction;

import java.util.List;

public interface Parser {
    List<Transaction> parse(String filePath);
}
