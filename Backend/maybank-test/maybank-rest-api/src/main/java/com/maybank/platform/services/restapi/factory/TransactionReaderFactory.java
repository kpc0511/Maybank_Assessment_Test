package com.maybank.platform.services.restapi.factory;

import org.springframework.batch.item.file.FlatFileItemReader;

public interface TransactionReaderFactory<T> {
    FlatFileItemReader<T> createReader(String filePath) throws Exception;
}
