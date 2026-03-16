package com.bank.gugu.user.repository;

import com.bank.gugu.common.model.constant.StatusType;
import com.bank.gugu.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserId(String userId);
    boolean existsByEmailAndStatus(String email, StatusType status);
    Optional<User> findByUserIdAndStatus(String userId, StatusType statusType);
    Optional<User> findByIdAndStatus(Long userNo, StatusType statusType);
    Optional<User> findByEmailAndStatus(String email, StatusType statusType);
    boolean existsByUserIdAndEmailAndStatus(String userId, String email, StatusType statusType);
}
