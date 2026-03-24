package com.bank.gugu.assetsDetail.input;

import com.bank.gugu.assetsDetail.repository.condition.AssetsCondition;
import com.bank.gugu.common.model.constant.RecordType;
import com.bank.gugu.global.query.record.Range;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@ToString
public class AssetsDetailsInput {
        @Schema(description = "계좌 아이디", example = "2")
        private Long assetsId;

        @Schema(description = "키워드", example = "입금")
        private String keyword;

        @Schema(description = "조회 시작 기간", example = "2025-05-25")
        @NotBlank(message = "조회시작 기간은 필수입니다.")
        private String startDate;

        @Schema(description = "조회 마지막 기간", example = "2025-06-25")
        @NotBlank(message = "조회 마지막 기간은 필수입니다.")
        private String endDate;

        @Schema(description = "거래유형", example = "ALL")
        @NotNull(message = "거래유형은 필수입니다.")
        private RecordType type;

        @Schema(description = "정렬 차순", example = "DESC")
        @NotNull(message = "정렬 차순은 필수입니다.")
        private String sort;

    public AssetsCondition toCondition() {
        return new AssetsCondition(
                this.assetsId,
                this.keyword,
                this.type,
                this.sort,
                new Range(LocalDate.parse(this.startDate), LocalDate.parse(endDate))
        );
    }

}
