package com.bank.gugu.record.service.dto.request;

import com.bank.gugu.record.model.Records;
import io.swagger.v3.oas.annotations.media.Schema;

public record RecordUpdateMemoRequest(
        @Schema(description = "메모", example = "마트 장보기")
        String memo
) {
        public Records toEntity(){
            return Records.builder()
                    .memo(this.memo)
                    .build();
        }
}
