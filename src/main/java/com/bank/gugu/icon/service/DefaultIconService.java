package com.bank.gugu.icon.service;

import com.bank.gugu.icon.repository.IconRepository;
import com.bank.gugu.icon.service.dto.response.IconsResponse;
import com.bank.gugu.common.model.constant.StatusType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DefaultIconService implements IconService {

    private final IconRepository iconRepository;

    @Override
    public List<IconsResponse> getIcons() {
        return iconRepository.findAllByStatus(StatusType.ACTIVE).stream()
                .map(IconsResponse::new)
                .toList();
    }
}
