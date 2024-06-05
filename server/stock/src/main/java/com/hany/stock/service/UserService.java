package com.hany.stock.service;

import com.hany.stock.auth.JwtTokenUtil;
import com.hany.stock.domain.dto.UserCntDto;
import com.hany.stock.domain.dto.UserDto;
import com.hany.stock.domain.dto.UserJoinRequest;
import com.hany.stock.domain.dto.UserLoginRequest;
import com.hany.stock.domain.entity.Comment;
import com.hany.stock.domain.entity.Like;
import com.hany.stock.domain.entity.User;
import com.hany.stock.enum_class.UserRole;
import com.hany.stock.repository.CommentRepository;
import com.hany.stock.repository.LikeRepository;
import com.hany.stock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    // 회원가입 유효성 검사
    public BindingResult joinValid(UserJoinRequest userJoinRequest, BindingResult bindingResult)
    {
        if (userJoinRequest.getLoginId().isEmpty()) {
            bindingResult.addError(new FieldError("userJoinRequest", "loginId", "아이디가 비어있습니다."));
        } else if (userJoinRequest.getLoginId().length() > 10) {
            bindingResult.addError(new FieldError("userJoinRequest", "loginId", "아이디가 10자가 넘습니다."));
        } else if (userRepository.existsByLoginId(userJoinRequest.getLoginId())) {
            bindingResult.addError(new FieldError("userJoinRequest", "loginId", "아이디가 중복됩니다."));
        }

        if (userJoinRequest.getPassword().isEmpty()) {
            bindingResult.addError(new FieldError("userJoinRequest", "password", "비밀번호가 비어있습니다."));
        }

        if (!userJoinRequest.getPassword().equals(userJoinRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("userJoinRequest", "passwordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if (userJoinRequest.getNickname().isEmpty()) {
            bindingResult.addError(new FieldError("userJoinRequest", "nickname", "닉네임이 비어있습니다."));
        } else if (userJoinRequest.getNickname().length() > 10) {
            bindingResult.addError(new FieldError("userJoinRequest", "nickname", "닉네임이 10자가 넘습니다."));
        } else if (userRepository.existsByNickname(userJoinRequest.getNickname())) {
            bindingResult.addError(new FieldError("userJoinRequest", "nickname", "닉네임이 중복됩니다."));
        }

        return bindingResult;
    }

    // 회원가입
    public void join(UserJoinRequest joinRequest) {
        userRepository.save(joinRequest.toEntity( encoder.encode(joinRequest.getPassword()) ));
    }
    // 로그인
    public boolean login(UserLoginRequest userLoginRequest, BindingResult bindingResult) {
        Optional<User> user = userRepository.findByLoginId(userLoginRequest.getLoginId());

        if (userLoginRequest.getLoginId().isEmpty()) {
            bindingResult.addError(new FieldError("userLoginRequest", "loginId", "아이디가 비어있습니다."));
        }
        if (userLoginRequest.getPassword().isEmpty()) {
            bindingResult.addError(new FieldError("userLoginRequest", "password", "비밀번호가 비어있습니다."));
        }

        if (!user.isPresent() || !encoder.matches(userLoginRequest.getPassword(), user.get().getPassword())) {
            bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
            return false;
        }
        return true;
    }
    // 로그인 성공 후 jwt 쿠키 생성
    public Cookie createLoginCookie(String loginId, BindingResult bindingResult) {
        String jwtToken = JwtTokenUtil.createToken(loginId, "my-secret-key-123123", 1000 * 60 * 60);
        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setMaxAge(60 * 60); // 쿠키 유효 시간 : 1시간
        cookie.setHttpOnly(true); // XSS 공격 방지
        cookie.setPath("/"); // 전체 도메인에 쿠키 적용

        return cookie;
    }
    // ㅣoginid로 유저 찾기
    public User getLoginUserByLoginId(String loginId) {
        if(loginId == null) return null;

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

    public User myInfo(String loginId) {
        return userRepository.findByLoginId(loginId).get();
    }

    public BindingResult editValid(UserDto dto, BindingResult bindingResult, String loginId)
    {
        User loginUser = userRepository.findByLoginId(loginId).get();

        if (dto.getNowPassword().isEmpty()) {
            bindingResult.addError(new FieldError("dto", "nowPassword", "현재 비밀번호가 비어있습니다."));
        } else if (!encoder.matches(dto.getNowPassword(), loginUser.getPassword())) {
            bindingResult.addError(new FieldError("dto", "nowPassword", "현재 비밀번호가 틀렸습니다."));
        }

        if (!dto.getNewPassword().equals(dto.getNewPasswordCheck())) {
            bindingResult.addError(new FieldError("dto", "newPasswordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if (dto.getNickname().isEmpty()) {
            bindingResult.addError(new FieldError("dto", "nickname", "닉네임이 비어있습니다."));
        } else if (dto.getNickname().length() > 10) {
            bindingResult.addError(new FieldError("dto", "nickname", "닉네임이 10자가 넘습니다."));
        } else if (!dto.getNickname().equals(loginUser.getNickname()) && userRepository.existsByNickname(dto.getNickname())) {
            bindingResult.addError(new FieldError("dto", "nickname", "닉네임이 중복됩니다."));
        }

        return bindingResult;
    }

    @Transactional
    public void edit(UserDto dto, String loginId) {
        User loginUser = userRepository.findByLoginId(loginId).get();

        if (dto.getNewPassword().equals("")) {
            loginUser.edit(loginUser.getPassword(), dto.getNickname());
        } else {
            loginUser.edit(encoder.encode(dto.getNewPassword()), dto.getNickname());
        }
    }

    @Transactional
    public Boolean delete(String loginId, String nowPassword) {
        User loginUser = userRepository.findByLoginId(loginId).get();

        if (encoder.matches(nowPassword, loginUser.getPassword())) {
            List<Like> likes = likeRepository.findAllByUserLoginId(loginId);
            for (Like like : likes) {
                like.getBoard().likeChange( like.getBoard().getLikeCnt() - 1 );
            }

            List<Comment> comments = commentRepository.findAllByUserLoginId(loginId);
            for (Comment comment : comments) {
                comment.getBoard().commentChange( comment.getBoard().getCommentCnt() - 1 );
            }

            userRepository.delete(loginUser);
            return true;
        } else {
            return false;
        }
    }

    public Page<User> findAllByNickname(String keyword, PageRequest pageRequest) {
        return userRepository.findAllByNicknameContains(keyword, pageRequest);
    }

    @Transactional
    public void changeRole(Long userId) {
        User user = userRepository.findById(userId).get();
        user.changeRole();
    }

    public UserCntDto getUserCnt() {
        return UserCntDto.builder()
                .totalUserCnt(userRepository.count())
                .totalAdminCnt(userRepository.countAllByUserRole(UserRole.ADMIN))
                .totalUserCnt(userRepository.countAllByUserRole(UserRole.USER))
                .build();
    }
}
