package com.hany.stock.domain.entity;

import com.hany.stock.domain.dto.BoardCreateRequest;
import com.hany.stock.domain.dto.BoardDto;
import com.hany.stock.enum_class.BoardCategory;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;   // 제목
    private String body;    // 본문

    @Enumerated(EnumType.STRING)
    private BoardCategory category; // 카테고리

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;      // 작성자

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Like> likes;       // 좋아요
    private Integer likeCnt;        // 좋아요 수

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Comment> comments; // 댓글
    private Integer commentCnt;     // 댓글 수
    @OneToOne(fetch = FetchType.LAZY)
    private UploadImage uploadImage;
    public void update(BoardDto dto) {
        this.title = dto.getTitle();
        this.body = dto.getBody();
    }

    public void updateCreateRequest(BoardCreateRequest req) {
        this.title = req.getTitle();
        this.body = req.getBody();
    }

    public void likeChange(Integer likeCnt) {
        this.likeCnt = likeCnt;
    }

    public void commentChange(Integer commentCnt) {
        this.commentCnt = commentCnt;
    }
    public void setUploadImage(UploadImage uploadImage) {
        this.uploadImage = uploadImage;
    }


}
