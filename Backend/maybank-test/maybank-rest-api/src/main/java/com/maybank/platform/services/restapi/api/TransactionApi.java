package com.maybank.platform.services.restapi.api;

import com.maybank.platform.services.restapi.payload.request.TransactionRequest;
import com.maybank.platform.services.restapi.payload.request.TransactionUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/api/transactions")
public interface TransactionApi {

    @PostMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    ResponseEntity<Map<String, Object>> searchTransactions(
            @Valid @RequestBody TransactionRequest transactionRequest);

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    ResponseEntity<?> updateTransactionDesc(@PathVariable Long id,
            @Valid @RequestBody TransactionUpdateRequest transactionUpdateRequest);

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    ResponseEntity<Map<String, Object>> getTransactionById(@PathVariable Long id);
}
