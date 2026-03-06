package com.bank.gugu.recordsImage.repository;

import com.bank.gugu.recordsImage.repository.condition.RecordImagesMonthCondition;
import com.bank.gugu.recordsImage.service.response.RecordsImagesMonthResponse;
import com.bank.gugu.common.model.constant.StatusType;
import com.bank.gugu.user.model.User;
import com.bank.gugu.global.query.record.Range;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bank.gugu.record.model.QRecords.*;
import static com.bank.gugu.recordsImage.model.QRecordsImage.*;

@Repository
@RequiredArgsConstructor
public class RecordsImageRepositoryImpl implements RecordsImageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RecordsImagesMonthResponse> findMonthQuery(RecordImagesMonthCondition condition) {
        return queryFactory.select(Projections.constructor(
                        RecordsImagesMonthResponse.class,
                        recordsImage.id,
                        recordsImage.path,
                        records.useDate))
                .from(recordsImage)
                .join(records)
                .on(recordsImage.records.eq(records))
                .where(
                        notDeleteImage(),
                        notDeleteRecord(),
                        eqUser(condition.user()),
                        eqRange(condition.range())
                )
                .fetch();

    }

    private BooleanExpression eqRange(Range range) {
        return records.useDate.between(range.start(), range.end());
    }

    /**
     * 회원정보 검색
     */
    private BooleanExpression eqUser(User user) {
        return recordsImage.user.eq(user);
    }

    /**
     * 삭제되지 않은 입/출금 내역 데이터
     */
    private BooleanExpression notDeleteRecord() {
        return records.status.eq(StatusType.ACTIVE);
    }

    /**
     * 삭제되지 않은 이미지 데이터
     */
    private BooleanExpression notDeleteImage() {
        return recordsImage.status.eq(StatusType.ACTIVE);
    }
}
