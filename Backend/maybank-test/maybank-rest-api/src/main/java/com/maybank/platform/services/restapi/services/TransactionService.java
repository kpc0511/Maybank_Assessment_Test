package com.maybank.platform.services.restapi.services;

import com.maybank.platform.services.restapi.model.FileInfo;
import com.maybank.platform.services.restapi.model.Transaction;
import com.maybank.platform.services.restapi.model.dto.TransactionDto;
import com.maybank.platform.services.restapi.payload.request.TransactionUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {
    TransactionDto getTransactionById(Long id);
    Page<TransactionDto> getTransactions(Long customerId,
                                         List<String> accountNumbers,
                                         String description,
                                         Pageable pageable);
    void transactionSave(String fileLocation, FileInfo fileInfo);

    Transaction updateTransaction(Long id, TransactionUpdateRequest request);
}
