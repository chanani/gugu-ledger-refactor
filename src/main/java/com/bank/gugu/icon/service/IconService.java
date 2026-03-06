package com.bank.gugu.icon.service;

import com.bank.gugu.icon.service.dto.response.IconsResponse;

import java.util.List;

public interface IconService {

    /**
     * 아이콘 목록 조회
     * @return 아이콘 리스트
     */
    List<IconsResponse> getIcons();
}
