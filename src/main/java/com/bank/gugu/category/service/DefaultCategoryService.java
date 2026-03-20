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
        Icon icon = findActiveIconOrThrow(request.icon());
        Integer order = findTopOrder(user);
        Category category = CategoryMapper.fromCreateCategoryRequest(request, user, icon, order);
        categoryRepository.save(category);
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
        Icon icon = findActiveIconOrThrow(request.icon());
        Category findCategory = findActiveCategoryOrThrow(categoryId);
        Category category = CategoryMapper.fromUpdateCategoryRequest(request, user, icon);
        findCategory.update(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId, User user) {
        Category category = categoryRepository.findByIdAndUserIdAndStatus(categoryId, user.getId(), StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_CATEGORY));
        category.remove();
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
        Category category = findActiveCategoryOrThrow(categoryId);
        return new CategoryResponse(category);
    }

    @Override
    @Transactional
    public void updateOrder(List<CategoryUpdateOrderRequest> request, User user) {
        List<Category> categories = findActiveCategories(user);
        Map<Long, Integer> orderUpdateMap = toOrderUpdateMap(request);

        categories.stream()
                .filter(category -> orderUpdateMap.containsKey(category.getId()))
                .forEach(category -> category.updateOrder(orderUpdateMap.get(category.getId())));
    }

    private List<Category> findActiveCategories(User user) {
        return categoryRepository.findAllByUserIdAndStatus(user.getId(), StatusType.ACTIVE);
    }

    private Map<Long, Integer> toOrderUpdateMap(List<CategoryUpdateOrderRequest> request) {
        return request.stream()
                .collect(Collectors.toMap(
                        CategoryUpdateOrderRequest::id,
                        CategoryUpdateOrderRequest::order
                ));
    }

    private Category findActiveCategoryOrThrow(Long categoryId) {
        return categoryRepository.findByIdAndStatus(categoryId, StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_CATEGORY));
    }


}
