package com.miracle.userservice.dto.request;

import com.miracle.userservice.dto.request.validation.util.ValidationDefaultMsgUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginRequestDto {

    @Schema(
            description = "유저 이메일. 이메일은 값이 입력되어야 함",
            required = true,
            example = "miracle@gmail.com"
    )
    @NotBlank(message = ValidationDefaultMsgUtil.UserLogin.EMAIL)
    private final String email;

    @Schema(
            description = "유저 비밀번호. 비밀번호 값이 입력되어야 함",
            required = true,
            example = "password!"
    )
    @NotBlank(message = ValidationDefaultMsgUtil.UserLogin.PASSWORD)
    private final String password;

    public UserLoginRequestDto() {
        this.email = null;
        this.password = null;
    }

    @Builder
    public UserLoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
