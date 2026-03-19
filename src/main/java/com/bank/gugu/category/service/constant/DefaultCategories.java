package com.bank.gugu.category.service.constant;

import com.bank.gugu.category.model.Category;
import com.bank.gugu.common.model.constant.RecordType;
import com.bank.gugu.icon.model.Icon;
import com.bank.gugu.user.model.User;

import java.util.List;
import java.util.Map;

public class DefaultCategories {

    private DefaultCategories() {}

    public static List<Category> of(User user, Map<String, List<Icon>> icons) {
        return List.of(
                // 지출
                new Category(user, RecordType.WITHDRAW, "주거비", 0, icons.get("house").getFirst()),
                new Category(user, RecordType.WITHDRAW, "식비", 1, icons.get("food").getFirst()),
                new Category(user, RecordType.WITHDRAW, "교통", 2, icons.get("bus").getFirst()),
                new Category(user, RecordType.WITHDRAW, "공과금", 3, icons.get("paper").getFirst()),
                new Category(user, RecordType.WITHDRAW, "의료비", 4, icons.get("hospital").getFirst()),
                new Category(user, RecordType.WITHDRAW, "여행", 5, icons.get("airplain").getFirst()),
                new Category(user, RecordType.WITHDRAW, "생필품/마트", 6, icons.get("cart").getFirst()),
                new Category(user, RecordType.WITHDRAW, "선물/경조사", 7, icons.get("present").getFirst()),
                new Category(user, RecordType.WITHDRAW, "구독료", 8, icons.get("netflix").getFirst()),
                new Category(user, RecordType.WITHDRAW, "통신비", 9, icons.get("phone").getFirst()),
                new Category(user, RecordType.WITHDRAW, "운동", 10, icons.get("health2").getFirst()),
                new Category(user, RecordType.WITHDRAW, "교육", 11, icons.get("study").getFirst()),
                new Category(user, RecordType.WITHDRAW, "미용", 12, icons.get("hair").getFirst()),
                new Category(user, RecordType.WITHDRAW, "의류", 13, icons.get("shirt").getFirst()),
                // 수입
                new Category(user, RecordType.DEPOSIT, "월급", 14, icons.get("money").getFirst()),
                new Category(user, RecordType.DEPOSIT, "용돈", 15, icons.get("coin").getFirst())
        );
    }

}
