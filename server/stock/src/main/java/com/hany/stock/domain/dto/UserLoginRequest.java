package com.hany.stock.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginRequest {
    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    private String loginId;
    @NotBlank(message = "비밀번호가 비어있습니다.")
    private String password;

}
