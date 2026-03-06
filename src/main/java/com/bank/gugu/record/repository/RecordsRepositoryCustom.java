package com.bank.gugu.record.repository;

import com.bank.gugu.graph.service.condition.GraphsCondition;
import com.bank.gugu.graph.service.dto.response.GraphCategories;
import com.bank.gugu.record.repository.condition.RecordCalendarCondition;
import com.bank.gugu.record.repository.condition.RecordCurrentCondition;
import com.bank.gugu.record.repository.condition.RecordMonthCondition;
import com.bank.gugu.record.service.dto.response.RecordsCalendarDetail;
import com.bank.gugu.record.service.dto.response.RecordsCurrentResponse;
import com.bank.gugu.record.service.dto.response.RecordsMonthResponse;

import java.util.List;

public interface RecordsRepositoryCustom {

    /**
     * 입/출금 내역 조회(하루)
     * @param condition 검색 객체
     * @return 입/출금 내역
     */
    List<RecordsCurrentResponse> findCurrentQuery(RecordCurrentCondition condition);

    /**
     * 입/출금 내역 조회(한달)
     * @param condition 검색 객체
     * @return 입/출금 내역
     */
    List<RecordsMonthResponse> findMonthQuery(RecordMonthCondition condition);

    /**
     * 입/출금 내역 그래프 내역 조회
     * @param condition 검색 객체
     * @return 그래프에서 사용할 내역 조회
     */
    List<GraphCategories> findGraphQuery(GraphsCondition condition);

    /**
     * 캘린더에 표시할 입/출금 및 총 금액 내역 조회
     * @param condition 검색 객체
     * @return 일별 입/출금 합계 내역
     */
    List<RecordsCalendarDetail> findCalendarQuery(RecordCalendarCondition condition);
}
