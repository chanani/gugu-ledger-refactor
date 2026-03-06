package com.bank.gugu.assetsDetail.repository;

import com.bank.gugu.assetsDetail.model.AssetsDetail;
import com.bank.gugu.common.model.constant.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetsDetailRepository extends JpaRepository<AssetsDetail, Long>, AssetsDetailRepositoryCustom {
    Optional<AssetsDetail> findByIdAndStatus(Long assetsDetailId, StatusType statusType);

    Optional<AssetsDetail> findByRecordIdAndStatus(Long id, StatusType statusType);
}
