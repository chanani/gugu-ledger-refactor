package com.bank.gugu.graph.service.condition;

import com.bank.gugu.common.model.constant.RecordType;
import com.bank.gugu.user.model.User;
import com.bank.gugu.global.query.record.Range;

public record GraphsCondition(
        // 날짜
        Range range,
        // 입/출금 타입
        RecordType type,
        // 회원 객체
        User user
){

}
