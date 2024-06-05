package com.hany.stock.service;

//import com.hany.stock.domain.dto.CategoryCommentCreateRequest;
import com.hany.stock.domain.dto.CategoryCommentCreateRequest;
import com.hany.stock.domain.dto.CommentCreateRequest;
import com.hany.stock.domain.entity.Board;
import com.hany.stock.domain.entity.Comment;
import com.hany.stock.domain.entity.User;
import com.hany.stock.domain.entity.stock.StockComment;
import com.hany.stock.domain.entity.stock.Stocks;
import com.hany.stock.domain.entity.theme.ThemeComment;
import com.hany.stock.domain.entity.theme.Themes;
import com.hany.stock.enum_class.UserRole;
import com.hany.stock.repository.BoardRepository;
import com.hany.stock.repository.CommentRepository;
import com.hany.stock.repository.UserRepository;
import com.hany.stock.repository.stock.StockCommentRepository;
import com.hany.stock.repository.theme.ThemeCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private final ThemeCommentRepository themeCommentRepository;
    private final StockCommentRepository stockCommentRepository;

    public void writeComment(Long boardId, CommentCreateRequest req, String loginId) {
        Board board = boardRepository.findById(boardId).get();
        User user = userRepository.findByLoginId(loginId).get();
        board.commentChange(board.getCommentCnt() + 1);
        commentRepository.save(req.toEntity(board, user));
    }

    public List<Comment> findAll(Long boardId) {
        return commentRepository.findAllByBoardId(boardId);
    }

    @Transactional
    public Long editComment(Long commentId, String newBody, String loginId) {
        Optional<Comment> optComment = commentRepository.findById(commentId);
        Optional<User> optUser = userRepository.findByLoginId(loginId);
        if (optComment.isEmpty() || optUser.isEmpty() || !optComment.get().getUser().equals(optUser.get())) {
            return null;
        }

        Comment comment = optComment.get();
        comment.update(newBody);

        return comment.getBoard().getId();
    }

    public Long deleteComment(Long commentId, String loginId) {
        Optional<Comment> optComment = commentRepository.findById(commentId);
        Optional<User> optUser = userRepository.findByLoginId(loginId);
        if (optComment.isEmpty() || optUser.isEmpty() ||
                (!optComment.get().getUser().equals(optUser.get()) && !optUser.get().getUserRole().equals(UserRole.ADMIN))) {
            return null;
        }

        Board board = optComment.get().getBoard();
        board.commentChange(board.getCommentCnt() - 1);

        commentRepository.delete(optComment.get());
        return board.getId();
    }
    public void writeStockComment(Stocks stock, CategoryCommentCreateRequest req, String loginId) {
        User user = userRepository.findByLoginId(loginId).get();
        stockCommentRepository.save(req.StockCommentToEntity(stock, user));
    }

    public void writeThemeComment(Themes theme, CategoryCommentCreateRequest req, String loginId) {
        User user = userRepository.findByLoginId(loginId).get();
        themeCommentRepository.save(req.ThemeCommentToEntity(theme, user));
    }
    public List<ThemeComment> findAllThemeComment(Themes theme) {
        return themeCommentRepository.findAllByTheme(theme);
    }
    public List<StockComment> findAllStockComment(Stocks stock) {
        return stockCommentRepository.findAllByStock(stock);
    }
    @Transactional
    public Long editThemeComment(Long commentId, String newBody, String loginId) {
        Optional<ThemeComment> optComment = themeCommentRepository.findById(commentId);
        Optional<User> optUser = userRepository.findByLoginId(loginId);
        if (optComment.isEmpty() || optUser.isEmpty() || !optComment.get().getUser().equals(optUser.get())) {
            return null;
        }
        ThemeComment comment = optComment.get();
        comment.update(newBody);
        return comment.getId();
    }

    @Transactional
    public Long editStockComment(Long commentId, String newBody, String loginId) {
        Optional<StockComment> optComment = stockCommentRepository.findById(commentId);
        Optional<User> optUser = userRepository.findByLoginId(loginId);
        if (optComment.isEmpty() || optUser.isEmpty() || !optComment.get().getUser().equals(optUser.get())) {
            return null;
        }
        StockComment comment = optComment.get();
        comment.update(newBody);
        return comment.getId();
    }


    public Long deleteThemeComment(Long commentId, String loginId) {
        Optional<ThemeComment> optComment = themeCommentRepository.findById(commentId);
        Optional<User> optUser = userRepository.findByLoginId(loginId);
        if (optComment.isEmpty() || optUser.isEmpty() ||
                (!optComment.get().getUser().equals(optUser.get()) && !optUser.get().getUserRole().equals(UserRole.ADMIN))) {
            return null;
        }
        themeCommentRepository.delete(optComment.get());
        return commentId;
    }

    public Long deleteStockComment(Long commentId, String loginId) {
        Optional<StockComment> optComment = stockCommentRepository.findById(commentId);
        Optional<User> optUser = userRepository.findByLoginId(loginId);
        if (optComment.isEmpty() || optUser.isEmpty() ||
                (!optComment.get().getUser().equals(optUser.get()) && !optUser.get().getUserRole().equals(UserRole.ADMIN))) {
            return null;
        }
        stockCommentRepository.delete(optComment.get());
        return commentId;
    }

}
