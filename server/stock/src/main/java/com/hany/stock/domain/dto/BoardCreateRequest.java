package com.hany.stock.domain.dto;

import com.hany.stock.domain.entity.Board;
import com.hany.stock.domain.entity.User;
import com.hany.stock.enum_class.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@AllArgsConstructor
@NoArgsConstructor  // Lombok을 사용해 기본 생성자 추가
public class BoardCreateRequest {

    private String title;
    private String body;
    private MultipartFile uploadImage;



    public Board toEntity(BoardCategory category, User user) {
        return Board.builder()
                .user(user)
                .category(category)
                .title(title)
                .body(body)
                .likeCnt(0)
                .commentCnt(0)
                .build();
    }
}
