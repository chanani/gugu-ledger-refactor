package com.bank.gugu.record.repository;

import com.bank.gugu.common.model.constant.StatusType;
import com.bank.gugu.record.model.Records;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecordsRepository extends JpaRepository<Records, Long>, RecordsRepositoryCustom {
    Optional<Records> findByIdAndStatus(Long recordsId, StatusType statusType);
}
