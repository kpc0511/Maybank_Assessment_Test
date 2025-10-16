package com.maybank.platform.services.restapi.services.impl;

import cn.hutool.core.util.ObjectUtil;
import com.maybank.platform.services.restapi.annotation.EnablePerformanceLogger;
import com.maybank.platform.services.restapi.common.LocaleMsgUtils;
import com.maybank.platform.services.restapi.dao.AccountRepository;
import com.maybank.platform.services.restapi.dao.TransactionRepository;
import com.maybank.platform.services.restapi.exceptions.OptimisticLockingFailureException;
import com.maybank.platform.services.restapi.exceptions.ResourceNotFoundException;
import com.maybank.platform.services.restapi.helper.TransactionHelper;
import com.maybank.platform.services.restapi.model.FileInfo;
import com.maybank.platform.services.restapi.model.Transaction;
import com.maybank.platform.services.restapi.model.dto.TransactionDto;
import com.maybank.platform.services.restapi.payload.request.TransactionUpdateRequest;
import com.maybank.platform.services.restapi.services.AccountService;
import com.maybank.platform.services.restapi.services.TransactionService;
import com.maybank.platform.services.util.RedisDbKeyUtils;
import com.maybank.platform.services.util.RedisUtil;
import com.maybank.platform.services.util.constants.MessagesConstant;
import com.maybank.platform.services.util.enums.EFileStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.maybank.platform.services.util.constants.GlobalConstants.GLOBAL_REDIS_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final FileInfoServiceImpl fileServiceImpl;
    private final AccountService accountService;
    private final RedisUtil redisUtil;

    @Override
    @Transactional(readOnly = true)
    public TransactionDto getTransactionById(Long id) {
        String key = RedisDbKeyUtils.getRedisCacheTransactionKey(id);
        if(redisUtil.exists(key)) {
            return redisUtil.get(key, TransactionDto.class);
        }
        TransactionDto transactionDto = transactionRepository.getTransactionById(id);
        if (ObjectUtil.isNotNull(transactionDto)) {
            redisUtil.put(key, transactionDto, 60 * 5);
        }
        return transactionDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDto> getTransactions(Long customerId,
                                                List<String> accountNumbers,
                                                String description,
                                                Pageable pageable) {
        if (accountNumbers == null) {
            return transactionRepository.searchTransactions(customerId, description, pageable);
        } else {
            return transactionRepository.searchTransactions(customerId, accountNumbers, description, pageable);
        }
    }

    @Override
    @EnablePerformanceLogger
    @Async
    @Transactional
    public void transactionSave(String fileLocation, FileInfo fileInfo) {
        try {
            TransactionHelper helper = new TransactionHelper(accountService);
            List<Transaction> list = helper.parseFile(fileLocation, fileInfo.getFileType().getKey());
            if(!CollectionUtils.isEmpty(list)) {
                FileInfo newFileInfo = fileInfo.toBuilder().fileStatus(EFileStatus.COMPLETE).build();
                newFileInfo.setCreateBy(fileInfo.getCreateBy());
                newFileInfo.setCreateDate(fileInfo.getCreateDate());
                newFileInfo.setUpdateDate(new Date());
                newFileInfo.setStatus(1);
                newFileInfo.setVersion(fileInfo.getVersion());
                newFileInfo.setUpdateBy("PROCESS_SCHEDULER");
                fileServiceImpl.save(newFileInfo);
                transactionRepository.saveAll(list);

                String pattern = GLOBAL_REDIS_KEY + "rest:api:transaction:*";
                redisUtil.deleteByPattern(pattern);
            }
        } catch(Exception e) {
            throw new RuntimeException("Filed to store csv data: " + e.getMessage());
        }
    }

    @Override
    @EnablePerformanceLogger
    @Transactional
    public Transaction updateTransaction(Long id, TransactionUpdateRequest request) {
        // Validate input
        if (ObjectUtils.isEmpty(request.getDescription()) || ObjectUtils.isEmpty(request.getVersion())) {
            throw new RuntimeException(LocaleMsgUtils.getMsg(MessagesConstant.PARAM_ERROR));
        }

        // Fetch the existing transaction
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(LocaleMsgUtils.getMsg(MessagesConstant.TRANSACTION_NOT_FOUND)));
        if(existingTransaction.getVersion().intValue() != request.getVersion().intValue()) {
            throw new OptimisticLockingFailureException(LocaleMsgUtils.getMsg(MessagesConstant.OPTIMISTIC_ERROR));
        }
        // Optimistic Locking Handling
        int updatedRows = transactionRepository.updateTransactionDescription(id, request.getDescription(), existingTransaction.getVersion());
        if (updatedRows == 0) {
            throw new OptimisticLockingFailureException(LocaleMsgUtils.getMsg(MessagesConstant.OPTIMISTIC_ERROR));
        }

        String key = RedisDbKeyUtils.getRedisCacheTransactionKey(id);
        redisUtil.delete(key);
        // Re-fetch the updated transaction to return the latest state (optional)
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(LocaleMsgUtils.getMsg(MessagesConstant.TRANSACTION_NOT_FOUND)));
    }
}
