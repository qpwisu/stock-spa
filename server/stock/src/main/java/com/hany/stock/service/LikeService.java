package com.hany.stock.service;

import com.hany.stock.domain.entity.Board;
import com.hany.stock.domain.entity.Like;
import com.hany.stock.domain.entity.User;
import com.hany.stock.repository.BoardRepository;
import com.hany.stock.repository.LikeRepository;
import com.hany.stock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void addLike(String loginId, Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        User loginUser = userRepository.findByLoginId(loginId).get();
        User boardUser = board.getUser();

        board.likeChange(board.getLikeCnt() + 1);

        likeRepository.save(Like.builder()
                        .user(loginUser)
                        .board(board)
                        .build());
    }

    @Transactional
    public void deleteLike(String loginId, Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        User loginUser = userRepository.findByLoginId(loginId).get();
        User boardUser = board.getUser();
        board.likeChange(board.getLikeCnt() - 1);
        likeRepository.deleteByUserLoginIdAndBoardId(loginId, boardId);
    }

    public Boolean checkLike(String loginId, Long boardId) {
        return likeRepository.existsByUserLoginIdAndBoardId(loginId, boardId);
    }
}
