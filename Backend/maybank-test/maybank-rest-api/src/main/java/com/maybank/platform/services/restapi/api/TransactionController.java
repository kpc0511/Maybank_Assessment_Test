package com.maybank.platform.services.restapi.api;

import com.maybank.platform.services.restapi.common.LocaleMsgUtils;
import com.maybank.platform.services.restapi.model.Transaction;
import com.maybank.platform.services.restapi.model.dto.TransactionDto;
import com.maybank.platform.services.restapi.payload.request.TransactionRequest;
import com.maybank.platform.services.restapi.payload.request.TransactionUpdateRequest;
import com.maybank.platform.services.restapi.payload.response.ApiResponse;
import com.maybank.platform.services.restapi.services.TransactionService;
import com.maybank.platform.services.util.constants.MessagesConstant;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TransactionController extends BaseController implements TransactionApi {
    private final TransactionService transactionService;

    @Override
    public ResponseEntity<Map<String, Object>> getTransactionById(@PathVariable Long id) {
        TransactionDto transactionDto = transactionService.getTransactionById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("data", transactionDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, Object>> searchTransactions(
            @Valid @RequestBody TransactionRequest transactionRequest) {
        List<Sort.Order> orders = new ArrayList<>();
        if (transactionRequest.getSort()[0].contains(",")) {
            // will sort more than 2 fields
            // sortOrder="field, direction"
            for (String sortOrder : transactionRequest.getSort()) {
                String[] sorts = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(sorts[1]), sorts[0]));
            }
        } else {
            // sort=[field, direction]
            orders.add(new Sort.Order(getSortDirection(transactionRequest.getSort()[1]), transactionRequest.getSort()[0]));
        }

        Pageable pagingSort = PageRequest.of(transactionRequest.getPage() - 1, transactionRequest.getSize(), Sort.by(orders));
        Page<TransactionDto> transactionPage = transactionService.getTransactions(transactionRequest.getCustomerId(),
                transactionRequest.getAccountNumbers(), transactionRequest.getDescription(), pagingSort);
        Map<String, Object> response = new HashMap<>();
        response.put("data", transactionPage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateTransactionDesc(@PathVariable Long id,
                                                   @Valid @RequestBody TransactionUpdateRequest transactionUpdateRequest) {
        try {
            Transaction updatedTransaction = transactionService.updateTransaction(id, transactionUpdateRequest);
            return ResponseEntity.ok(new ApiResponse(true, LocaleMsgUtils.getMsg(MessagesConstant.UPDATE_SUCCESS)));
        } catch (OptimisticLockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(false, LocaleMsgUtils.getMsg(MessagesConstant.UPDATE_CONFLICT_ERROR)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }
}
