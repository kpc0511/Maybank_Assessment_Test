package com.maybank.platform.services.restapi.factory;

import com.maybank.platform.services.restapi.factory.reader.CsvTransactionReader;
import com.maybank.platform.services.restapi.factory.reader.TxtTransactionReader;
import com.maybank.platform.services.restapi.model.dto.TransactionCsvDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TransactionReaderFactorySelector {
    private final Map<String, TransactionReaderFactory<TransactionCsvDto>> factories = new HashMap<>();

    // Constructor injection for all reader beans
    public TransactionReaderFactorySelector(CsvTransactionReader csvReader,
                                            TxtTransactionReader txtReader) {
        factories.put("csv", csvReader);
        factories.put("txt", txtReader);
    }

    public TransactionReaderFactory<TransactionCsvDto> getReader(String fileType) {
        if (fileType == null) throw new IllegalArgumentException("fileType cannot be null");

        TransactionReaderFactory<TransactionCsvDto> factory = factories.get(fileType.toLowerCase());
        if (factory == null) {
            throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
        return factory;
    }
}
