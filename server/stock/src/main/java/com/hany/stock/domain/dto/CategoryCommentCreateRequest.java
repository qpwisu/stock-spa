package com.hany.stock.domain.dto;
import com.hany.stock.domain.entity.stock.StockComment;
import com.hany.stock.domain.entity.User;
import com.hany.stock.domain.entity.stock.Stocks;
import com.hany.stock.domain.entity.theme.ThemeComment;
import com.hany.stock.domain.entity.theme.Themes;
import lombok.Data;

@Data
public class CategoryCommentCreateRequest {

    private String body;

    public StockComment StockCommentToEntity(Stocks stock, User user) {
        return StockComment.builder()
                .user(user)
                .stock(stock)
                .body(body)
                .build();
    }
    public ThemeComment ThemeCommentToEntity(Themes theme, User user) {
        return ThemeComment.builder()
                .user(user)
                .theme(theme)
                .body(body)
                .build();
    }
}
