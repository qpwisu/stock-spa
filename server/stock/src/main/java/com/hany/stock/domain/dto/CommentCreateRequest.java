package com.hany.stock.domain.dto;

import com.hany.stock.domain.entity.Board;
import com.hany.stock.domain.entity.Comment;
import com.hany.stock.domain.entity.User;
import lombok.Data;

@Data
public class CommentCreateRequest {

    private String body;

    public Comment toEntity(Board board, User user) {
        return Comment.builder()
                .user(user)
                .board(board)
                .body(body)
                .build();
    }
}
