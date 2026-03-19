package com.bank.gugu.category.service;

import com.bank.gugu.category.mapper.CategoryMapper;
import com.bank.gugu.category.repository.CategoryRepository;
import com.bank.gugu.category.service.constant.DefaultCategories;
import com.bank.gugu.category.service.dto.request.CategoryCreateRequest;
import com.bank.gugu.category.service.dto.request.CategoryUpdateOrderRequest;
import com.bank.gugu.category.service.dto.request.CategoryUpdateRequest;
import com.bank.gugu.category.service.dto.response.CategoriesResponse;
import com.bank.gugu.category.service.dto.response.CategoryResponse;
import com.bank.gugu.icon.repository.IconRepository;
import com.bank.gugu.category.model.Category;
import com.bank.gugu.common.model.constant.RecordType;
import com.bank.gugu.common.model.constant.StatusType;
import com.bank.gugu.icon.model.Icon;
import com.bank.gugu.user.model.User;
import com.bank.gugu.global.exception.OperationErrorException;
import com.bank.gugu.global.exception.dto.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DefaultCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final IconRepository iconRepository;

    @Override
    @Transactional
    public void addCategories(User user) {
        List<Category> categories = createDefaultCategories(user);
        categoryRepository.saveAll(categories);
    }

    private List<Category> createDefaultCategories(User user) {
        Map<String, List<Icon>> icons = iconRepository.findAll().stream()
                .collect(Collectors.groupingBy(Icon::getName));

        return DefaultCategories.of(user, icons);
    }

    @Override
    @Transactional
    public void addCategory(CategoryCreateRequest request, User user) {
        Icon findIcon = findActiveIconOrThrow(request.icon());
        Integer order = findTopOrder(user);
        Category newEntity = CategoryMapper.fromCreateCategoryRequest(request, user, findIcon, order);
        categoryRepository.save(newEntity);
    }

    private Icon findActiveIconOrThrow(Integer iconId) {
        if (iconId == null || iconId == 0) {
            return null;
        }
        return iconRepository.findByIdAndStatus(iconId, StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_ICON));
    }

    private Integer findTopOrder(User user) {
        return categoryRepository.findTopOrdersByUserAndStatus(user, StatusType.ACTIVE)
                .orElse(0);
    }

    @Override
    @Transactional
    public void updateCategory(Long categoryId, CategoryUpdateRequest request, User user) {
        // 아이콘 전달 했을 경우 아이콘 조회
        Icon findIcon = findActiveIconOrThrow(request.icon());
        // 카테고리 조회
        Category findCategory = categoryRepository.findByIdAndStatus(categoryId, StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_CATEGORY));
        // dto -> entity
        Category newEntity = request.toEntity(user, findIcon);
        // 수정 진행
        findCategory.update(newEntity);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        // 카테고리 조회
        Category findCategory = categoryRepository.findByIdAndStatus(categoryId, StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_CATEGORY));
        // 카테고리 소프트 삭제
        findCategory.remove();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriesResponse> getCategories(User user, RecordType type) {
        if (type.equals(RecordType.ALL)) {
            return categoryRepository.findByUserIdAndStatusOrderByOrdersAsc(user.getId(), StatusType.ACTIVE).stream()
                    .map(CategoriesResponse::new)
                    .toList();
        }
        return categoryRepository.findByUserIdAndTypeAndStatusOrderByOrdersAsc(user.getId(), type, StatusType.ACTIVE).stream()
                .map(CategoriesResponse::new)
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategory(Long categoryId) {
        Category findCategory = categoryRepository.findByIdAndStatus(categoryId, StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_CATEGORY));
        return new CategoryResponse(findCategory);
    }

    @Override
    @Transactional
    public void updateOrder(List<CategoryUpdateOrderRequest> request, User user) {
        // 카테고리 목록 조회
        List<Category> categories = categoryRepository.findAllByUserIdAndStatus(user.getId(), StatusType.ACTIVE);

        // 요청받은 순서 업데이트 정보를 Map으로 변환 (빠른 검색을 위해)
        Map<Long, Integer> orderUpdateMap = request.stream()
                .collect(Collectors.toMap(
                        CategoryUpdateOrderRequest::id,
                        CategoryUpdateOrderRequest::order
                ));

        // 각 카테고리의 순서 업데이트
        categories.stream()
                .filter(category -> orderUpdateMap.containsKey(category.getId()))
                .forEach(category -> {
                    Integer newOrder = orderUpdateMap.get(category.getId());
                    category.updateOrder(newOrder);
                });
    }

    /**
     * 카테고리를 뒤쪽으로 이동 (순서 증가)
     */
    private void moveBackward(User user, Category targetCategory, Integer currentOrder, Integer requestOrder) {
        // 1. 이동할 범위의 카테고리들을 앞으로 한 칸씩 이동
        // currentOrder + 1부터 requestOrder까지의 카테고리들을 -1씩 이동
        List<Category> categoriesToShift = categoryRepository.findByUserAndOrdersBetween(
                user, currentOrder + 1, requestOrder);

        for (Category category : categoriesToShift) {
            category.updateOrder(category.getOrders() - 1);
        }

        // 2. 대상 카테고리를 새 위치로 이동
        targetCategory.updateOrder(requestOrder);

        // 3. 변경사항 저장
        categoryRepository.saveAll(categoriesToShift);
        categoryRepository.save(targetCategory);
    }

    /**
     * 카테고리를 앞쪽으로 이동 (순서 감소)
     */
    private void moveForward(User user, Category targetCategory, Integer currentOrder, Integer requestOrder) {
        // 1. 이동할 범위의 카테고리들을 뒤로 한 칸씩 이동
        // requestOrder부터 currentOrder - 1까지의 카테고리들을 +1씩 이동
        List<Category> categoriesToShift = categoryRepository.findByUserAndOrdersBetween(
                user, requestOrder, currentOrder - 1);

        for (Category category : categoriesToShift) {
            category.updateOrder(category.getOrders() + 1);
        }

        // 2. 대상 카테고리를 새 위치로 이동
        targetCategory.updateOrder(requestOrder);

        // 3. 변경사항 저장
        categoryRepository.saveAll(categoriesToShift);
        categoryRepository.save(targetCategory);
    }


}
