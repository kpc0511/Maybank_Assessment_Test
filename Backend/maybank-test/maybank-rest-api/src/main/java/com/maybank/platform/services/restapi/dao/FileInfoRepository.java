package com.maybank.platform.services.restapi.dao;

import com.maybank.platform.services.restapi.model.FileInfo;
import com.maybank.platform.services.util.enums.EFileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    List<FileInfo> findByFileStatusAndVersionOrderByCreateDateAsc(EFileStatus fileStatus, Integer version);

    @Transactional
    @Modifying
    @Query("UPDATE FileInfo f SET f.version = 2 WHERE f.id = :id AND f.version = 1")
    void updateFileInfoById(@Param("id") Long id);
}
