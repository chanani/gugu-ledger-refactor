package com.bank.gugu.category;

import com.bank.gugu.category.service.CategoryService;
import com.bank.gugu.category.service.dto.request.CategoryCreateRequest;
import com.bank.gugu.category.service.dto.request.CategoryUpdateOrderRequest;
import com.bank.gugu.category.service.dto.request.CategoryUpdateRequest;
import com.bank.gugu.category.service.dto.response.CategoriesResponse;
import com.bank.gugu.category.service.dto.response.CategoryResponse;
import com.bank.gugu.common.model.constant.RecordType;
import com.bank.gugu.global.annotation.AuthUser;
import com.bank.gugu.user.model.User;
import com.bank.gugu.global.response.ApiResponse;
import com.bank.gugu.global.response.DataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category API Controller", description = "카테고리 관련 API를 제공합니다.")
@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerDocs {

    private final CategoryService categoryService;

    @PostMapping("/api/v1/user/categories")
    @Override
    public ResponseEntity<ApiResponse> addCategory(
            @Valid @RequestBody CategoryCreateRequest request,
            @AuthUser User user
    ) {
        categoryService.addCategory(request, user);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @PutMapping("/api/v1/user/categories/{categoryId}")
    @Override
    public ResponseEntity<ApiResponse> updateCategory(
            @PathVariable(name = "categoryId") Long categoryId,
            @Valid @RequestBody CategoryUpdateRequest request,
            @Parameter(hidden = true) User user
    ) {
        categoryService.updateCategory(categoryId, request, user);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @DeleteMapping("/api/v1/user/categories/{categoryId}")
    @Override
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable(name = "categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping("/api/v1/user/categories")
    @Override
    public ResponseEntity<DataResponse<List<CategoriesResponse>>> getCategories(
            @Parameter(hidden = true) User user,
            @Parameter(name = "type") RecordType type
    ) {
        List<CategoriesResponse> categories = categoryService.getCategories(user, type);
        return ResponseEntity.ok(DataResponse.send(categories));
    }

    @Operation(summary = "카테고리 상세 정보 조회 API",
            description = "카테고리의 상세 정보를 조회합니다.")
    @GetMapping("/api/v1/user/categories/{categoryId}")
    public ResponseEntity<DataResponse<CategoryResponse>> getCategory(
            @PathVariable(name = "categoryId") Long categoryId
    ) {
        CategoryResponse categories = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(DataResponse.send(categories));
    }

    @Operation(summary = "카테고리 순서 변경 API",
            description = "카테고리 순서를 변경합니다.")
    @PutMapping("/api/v1/user/categories-order")
    public ResponseEntity<ApiResponse> updateCategoryOrder(
            @Valid @RequestBody List<CategoryUpdateOrderRequest> request,
            @Parameter(hidden = true) User user
    ) {
        categoryService.updateOrder(request, user);
        return ResponseEntity.ok(ApiResponse.ok());
    }


}
