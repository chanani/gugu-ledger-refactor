package com.bank.gugu.recordsImage.repository;

import com.bank.gugu.common.model.constant.StatusType;
import com.bank.gugu.record.model.Records;
import com.bank.gugu.recordsImage.model.RecordsImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecordsImageRepository extends JpaRepository<RecordsImage, Long>, RecordsImageRepositoryCustom {
    List<RecordsImage> findAllByRecordsAndStatus(Records findRecord, StatusType statusType);

    Optional<RecordsImage> findByIdAndStatus(Long id, StatusType statusType);

    List<RecordsImage> findByIdInAndStatus(List<Long> deleteIds, StatusType statusType);
}
