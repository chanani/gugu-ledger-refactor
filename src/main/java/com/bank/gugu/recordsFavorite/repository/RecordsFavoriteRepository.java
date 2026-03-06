package com.bank.gugu.recordsFavorite.repository;

import com.bank.gugu.common.model.constant.StatusType;
import com.bank.gugu.recordsFavorite.model.RecordsFavorite;
import com.bank.gugu.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecordsFavoriteRepository extends JpaRepository<RecordsFavorite, Long> {
    Optional<RecordsFavorite> findByIdAndStatus(Long recordsFavoriteId, StatusType statusType);

    List<RecordsFavorite> findAllByUserAndStatusOrderById(User user, StatusType statusType);
}
