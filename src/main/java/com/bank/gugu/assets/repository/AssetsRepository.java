package com.bank.gugu.assets.repository;

import com.bank.gugu.assets.model.Assets;
import com.bank.gugu.common.model.constant.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetsRepository extends JpaRepository<Assets, Long> {
    Optional<Assets> findByIdAndStatus(Long assetsId, StatusType statusType);
    List<Assets> findAllByUserIdAndStatusOrderByOrdersAsc(Long id, StatusType statusType);
}
