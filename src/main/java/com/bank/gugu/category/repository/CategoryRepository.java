package com.bank.gugu.category.repository;

import com.bank.gugu.category.model.Category;
import com.bank.gugu.common.model.constant.RecordType;
import com.bank.gugu.common.model.constant.StatusType;
import com.bank.gugu.user.model.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByIdAndStatus(Long categoryId, StatusType statusType);

    List<Category> findByUserIdAndTypeAndStatusOrderByOrdersAsc(Long id, RecordType type, StatusType statusType);

    List<Category> findByUserIdAndStatusOrderByOrdersAsc(Long userId, StatusType status);

    Optional<Category> findByUserAndOrders(User user, Integer currentOrder);

    List<Category> findByUserAndOrdersBetween(User user, int currentOrder, Integer requestOrder);

    @Query("SELECT c.orders FROM Category c WHERE c.user = :user AND c.status = :status ORDER BY c.orders DESC LIMIT 1")
    Optional<Integer> findTopOrdersByUserAndStatus(@Param("user") User user, @Param("status") StatusType status);

    List<Category> findAllByUserIdAndStatus(Long id, StatusType statusType);
}
