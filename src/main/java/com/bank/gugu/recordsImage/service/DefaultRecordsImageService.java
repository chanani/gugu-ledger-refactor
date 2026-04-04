package com.bank.gugu.recordsImage.service;

import com.bank.gugu.record.repository.RecordsRepository;
import com.bank.gugu.recordsImage.repository.RecordsImageRepository;
import com.bank.gugu.recordsImage.repository.condition.RecordImagesMonthCondition;
import com.bank.gugu.recordsImage.service.response.RecordsImagesMonthResponse;
import com.bank.gugu.recordsImage.service.response.RecordsImagesResponse;
import com.bank.gugu.common.model.constant.StatusType;
import com.bank.gugu.record.model.Records;
import com.bank.gugu.recordsImage.model.RecordsImage;
import com.bank.gugu.user.model.User;
import com.bank.gugu.global.exception.OperationErrorException;
import com.bank.gugu.global.exception.dto.ErrorCode;
import com.bank.gugu.global.query.record.Range;
import com.bank.gugu.global.utils.FileUtil;
import com.bank.gugu.global.utils.dto.FileName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DefaultRecordsImageService implements RecordsImageService {

    public static final String FILE_PATH = "/bank/fileImage";

    private final RecordsImageRepository recordsImageRepository;
    private final RecordsRepository recordsRepository;
    private final FileUtil fileUtil;

    @Override
    @Transactional
    public void deleteRecordImage(Long recordsImageId) {
        RecordsImage deleteRecordImage = findByRecordsImageOrThrow(recordsImageId);
        deleteRecordImage.remove();
    }

    @Override
    @Transactional
    public void addRecordImage(Long recordsId, MultipartFile inputFile, User user) {
        FileName fileName = fileUtil.fileUpload(inputFile, FILE_PATH);
        Records records = findRecordsOrThrow(recordsId);
        RecordsImage newEntity = RecordsImage.builder()
                .records(records)
                .path(fileName.getModifiedFileName())
                .user(user)
                .build();
        recordsImageRepository.save(newEntity);
    }

    @Override
    public List<RecordsImagesResponse> getRecordsImages(String date, User user) {
        // 해당 월의 첫 번째 날과 마지막 날 계산
        LocalDate startDate = LocalDate.parse(date.concat("-01"));
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // to condition
        RecordImagesMonthCondition condition = new RecordImagesMonthCondition(new Range(startDate, endDate), user);

        // 월별 이미지 목록 조회
        Map<LocalDate, List<RecordsImagesMonthResponse>> groupedByDate = recordsImageRepository.findMonthQuery(condition).stream()
                .collect(Collectors.groupingBy(RecordsImagesMonthResponse::getUseDate));

        return groupedByDate.entrySet().stream()
                .sorted(Map.Entry.<LocalDate, List<RecordsImagesMonthResponse>>comparingByKey().reversed())
                .map(entry -> new RecordsImagesResponse(
                        entry.getKey().toString(),
                        entry.getValue()
                ))
                .toList();
    }

    private RecordsImage findByRecordsImageOrThrow(Long recordsImageId) {
        return recordsImageRepository.findByIdAndStatus(recordsImageId, StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_RECORDS_IMAGE));
    }

    private Records findRecordsOrThrow(Long recordsId) {
        return recordsRepository.findByIdAndStatus(recordsId, StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_RECORDS));
    }
}
