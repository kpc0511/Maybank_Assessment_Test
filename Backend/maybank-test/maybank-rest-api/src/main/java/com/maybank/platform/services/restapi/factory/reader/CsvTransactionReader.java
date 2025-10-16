package com.maybank.platform.services.restapi.factory.reader;

import com.maybank.platform.services.restapi.factory.TransactionReaderFactory;
import com.maybank.platform.services.restapi.model.dto.TransactionCsvDto;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class CsvTransactionReader implements TransactionReaderFactory<TransactionCsvDto> {
    @Override
    public FlatFileItemReader<TransactionCsvDto> createReader(String filePath) {
        FlatFileItemReader<TransactionCsvDto> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLinesToSkip(1);
        reader.setLineMapper(lineMapper());
        return reader;
    }

    private DefaultLineMapper<TransactionCsvDto> lineMapper() {
        DefaultLineMapper<TransactionCsvDto> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer("|");
        tokenizer.setNames("accountNumber", "trxAmount", "description", "trxDate", "trxTime", "customerId");
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSet -> {
            TransactionCsvDto dto = new TransactionCsvDto();
            dto.setAccountNumber(fieldSet.readString("accountNumber"));
            dto.setTrxAmount(fieldSet.readBigDecimal("trxAmount"));
            dto.setDescription(fieldSet.readString("description"));
            dto.setTrxDate(LocalDate.parse(fieldSet.readString("trxDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            dto.setTrxTime(LocalTime.parse(fieldSet.readString("trxTime"), DateTimeFormatter.ofPattern("HH:mm:ss")));
            dto.setCustomerId(fieldSet.readLong("customerId"));
            return dto;
        });
        return lineMapper;
    }
}
