package com.hany.stock.controller;

import com.hany.stock.domain.dto.UserDto;
import com.hany.stock.domain.dto.UserJoinRequest;
import com.hany.stock.domain.dto.UserLoginRequest;
import com.hany.stock.domain.entity.User;
import com.hany.stock.service.BoardService;
import com.hany.stock.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final BoardService boardService;


    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("userJoinRequest", new UserJoinRequest());
        return "users/join";
    }

    @PostMapping("/join")
    public String join( @ModelAttribute UserJoinRequest userJoinRequest, BindingResult bindingResult, Model model) {

        // Validation
        if (userService.joinValid(userJoinRequest, bindingResult).hasErrors()) {
            return "users/join";
        }

        userService.join(userJoinRequest);
        model.addAttribute("message", "회원가입에 성공했습니다!\n로그인 후 사용 가능합니다!");
        model.addAttribute("nextUrl", "/users/login");
        return "printMessage";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("userLoginRequest", new UserLoginRequest());
        return "users/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserLoginRequest userLoginRequest, BindingResult bindingResult,
                        HttpServletResponse response) {

        // 로그인
        if (!userService.login(userLoginRequest, bindingResult)) {
            return "users/login";
        }
        // 로그인 성공시 쿠키 생성
        Cookie jwtCookie = userService.createLoginCookie(userLoginRequest.getLoginId(), bindingResult);
        response.addCookie(jwtCookie);

        return "redirect:/";  // 로그인 후 리디렉트될 페이지
    }

    //logout은 securityconfig에서 설정
    @GetMapping("/myPage/{category}")
    public String myPage(@PathVariable String category, Authentication auth, Model model) {
        model.addAttribute("boards", boardService.findMyBoard(category, auth.getName()));
        model.addAttribute("category", category);
        model.addAttribute("user", userService.myInfo(auth.getName()));
        return "users/myPage";
    }

    @GetMapping("/edit")
    public String userEditPage(Authentication auth, Model model) {
        User user = userService.myInfo(auth.getName());
        model.addAttribute("userDto", UserDto.of(user));
        return "users/edit";
    }

    @PostMapping("/edit")
    public String userEdit(@Valid @ModelAttribute UserDto dto, BindingResult bindingResult,
                           Authentication auth, Model model) {

        // Validation
        if (userService.editValid(dto, bindingResult, auth.getName()).hasErrors()) {
            return "users/edit";
        }

        userService.edit(dto, auth.getName());

        model.addAttribute("message", "정보가 수정되었습니다.");
        model.addAttribute("nextUrl", "/users/myPage/board");
        return "printMessage";
    }

    @GetMapping("/delete")
    public String userDeletePage(Authentication auth, Model model) {
        User user = userService.myInfo(auth.getName());
        model.addAttribute("userDto", UserDto.of(user));
        return "users/delete";
    }

    @PostMapping("/delete")
    public String userDelete(@ModelAttribute UserDto dto, Authentication auth, Model model) {
        Boolean deleteSuccess = userService.delete(auth.getName(), dto.getNowPassword());
        if (deleteSuccess) {
            model.addAttribute("message", "탈퇴 되었습니다.");
            model.addAttribute("nextUrl", "/users/logout");
            return "printMessage";
        } else {
            model.addAttribute("message", "현재 비밀번호가 틀려 탈퇴에 실패하였습니다.");
            model.addAttribute("nextUrl", "/users/delete");
            return "printMessage";
        }
    }

    @GetMapping("/admin")
    public String adminPage(@RequestParam(required = false, defaultValue = "1") int page,
                            @RequestParam(required = false, defaultValue = "") String keyword,
                            Model model) {

        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        Page<User> users = userService.findAllByNickname(keyword, pageRequest);

        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        return "users/admin";
    }

    @GetMapping("/admin/{userId}")
    public String adminChangeRole(@PathVariable Long userId,
                                  @RequestParam(required = false, defaultValue = "1") int page,
                                  @RequestParam(required = false, defaultValue = "") String keyword) throws UnsupportedEncodingException {
        userService.changeRole(userId);
        return "redirect:/users/admin?page=" + page + "&keyword=" + URLEncoder.encode(keyword, "UTF-8");
    }

//    @GetMapping("/authentication-fail")
//    public String authenticationFail() {
//        return "errorPage/authenticationFail";
//    }
//
//    @GetMapping("/authorization-fail")
//    public String authorizationFail() {
//        return "errorPage/authorizationFail";
//    }
}
