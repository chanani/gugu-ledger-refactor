package com.bank.gugu.icon.repository;

import com.bank.gugu.common.model.constant.StatusType;
import com.bank.gugu.icon.model.Icon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IconRepository extends JpaRepository<Icon, Integer> {
    Optional<Icon> findByIdAndStatus(Integer icon, StatusType statusType);

    List<Icon> findAllByStatus(StatusType statusType);
}
