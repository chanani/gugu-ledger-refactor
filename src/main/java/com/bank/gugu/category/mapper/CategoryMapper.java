package com.bank.gugu.category.mapper;

import com.bank.gugu.category.model.Category;
import com.bank.gugu.category.service.dto.request.CategoryCreateRequest;
import com.bank.gugu.category.service.dto.request.CategoryUpdateRequest;
import com.bank.gugu.icon.model.Icon;
import com.bank.gugu.user.model.User;

public class CategoryMapper {

    private CategoryMapper() {
    }

    public static Category fromCreateCategoryRequest(
            CategoryCreateRequest request,
            User user,
            Icon icon,
            Integer order) {
        return Category.builder()
                .user(user)
                .type(request.type())
                .icon(icon)
                .name(request.name())
                .orders(order + 1)
                .build();
    }

    public static Category fromUpdateCategoryRequest(
            CategoryUpdateRequest request,
            User user,
            Icon icon
    ) {
        return Category.builder()
                .user(user)
                .type(request.type())
                .icon(icon)
                .name(request.name())
                .build();
    }
}
