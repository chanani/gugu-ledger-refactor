package com.bank.gugu.assetsDetail.repository;

import com.bank.gugu.assetsDetail.repository.condition.AssetsCondition;
import com.bank.gugu.assetsDetail.model.AssetsDetail;
import com.bank.gugu.user.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssetsDetailRepositoryCustom {
    List<AssetsDetail> findByQuery(Pageable pageable, AssetsCondition condition, User user);
}
