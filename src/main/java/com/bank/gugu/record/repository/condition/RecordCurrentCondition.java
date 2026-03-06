package com.bank.gugu.record.repository.condition;

import com.bank.gugu.user.model.User;

import java.time.LocalDate;

public record RecordCurrentCondition(
        // 날짜
        LocalDate currentDate,

        // 회원 객체
        User user
) {
}
