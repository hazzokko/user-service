package com.miracle.userservice.controller;

import com.miracle.userservice.controller.response.CommonApiResponse;
import com.miracle.userservice.controller.response.SuccessApiResponse;
import com.miracle.userservice.dto.request.UserJoinRequestDto;
import com.miracle.userservice.dto.request.UserLoginRequestDto;
import com.miracle.userservice.dto.request.UserUpdateInfoRequestDto;
import com.miracle.userservice.dto.request.validation.util.ValidationDefaultMsgUtil;
import com.miracle.userservice.dto.response.UserBaseInfoResponseDto;
import com.miracle.userservice.dto.response.UserInfoResponseDto;
import com.miracle.userservice.dto.response.UserListResponseDto;
import com.miracle.userservice.dto.response.UserLoginResponseDto;
import com.miracle.userservice.dto.response.UserLoginResponseDto.UserLoginResponseDtoBuilder;
import com.miracle.userservice.entity.User;
import com.miracle.userservice.exception.InvalidParameterException;
import com.miracle.userservice.service.UserService;
import com.miracle.userservice.swagger.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DefaultPathDocket
@RequiredArgsConstructor
@RequestMapping("/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @ApiPostUserLogin
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public CommonApiResponse login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
        Optional<User> userOpt = userService.login(userLoginRequestDto);
        boolean success = userOpt.isPresent();
        int httpStatus;
        String message;

        UserLoginResponseDtoBuilder builder = UserLoginResponseDto.builder()
                .success(success)
                .id(null)
                .email(null)
                .name(null);
        if (success) {
            User user = userOpt.get();
            httpStatus = HttpStatus.OK.value();
            message = "로그인에 성공했습니다.";
            builder
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName());
        } else {
            httpStatus = HttpStatus.BAD_REQUEST.value();
            message = "로그인에 실패했습니다.";
        }

        UserLoginResponseDto userLoginResponseDto = builder.build();
        response.setStatus(httpStatus);
        return new SuccessApiResponse<>(httpStatus, message, userLoginResponseDto);
    }

    @ApiPostUserJoin
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/join")
    public CommonApiResponse join(@Valid @RequestBody UserJoinRequestDto dto) {
        userService.join(dto);

        int httpStatus = HttpStatus.OK.value();
        String message = "회원 가입 성공";
        return new SuccessApiResponse<>(httpStatus, message, null);
    }

    @ApiGetCheckEmail
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/check-email/{email}")
    public CommonApiResponse checkDuplicateEmail(@PathVariable String email) {
        String message;
        Boolean data;
        if (userService.checkDuplicate(email)) {
            message = "사용할 수 없는 이메일입니다.";
            data = Boolean.TRUE;
        } else {
            message = "사용 가능한 이메일입니다.";
            data = Boolean.FALSE;
        }

        int httpStatus = HttpStatus.OK.value();
        return new SuccessApiResponse<>(httpStatus, message, data);
    }

    @UserPathDocket
    @ApiGetUserBaseInfo
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}/base-info")
    public CommonApiResponse getUserBaseInfo(@PathVariable Long userId) {
        UserBaseInfoResponseDto dto = userService.getUserBaseInfo(userId);

        int httpStatus = HttpStatus.OK.value();
        String message = "유저 기본 정보 조회 성공";
        return new SuccessApiResponse<>(httpStatus, message, dto);
    }

    @UserPathDocket
    @ApiGetUserInfo
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    public CommonApiResponse getUserInfo(@PathVariable Long userId) {
        UserInfoResponseDto dto = userService.getUserInfo(userId);

        int httpStatus = HttpStatus.OK.value();
        String message = "유저 정보 조회 성공";
        return new SuccessApiResponse<>(httpStatus, message, dto);
    }

    @UserPathDocket
    @ApiPutUserInfo
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{userId}")
    public CommonApiResponse updateUserInfo(@PathVariable Long userId, @Valid @RequestBody UserUpdateInfoRequestDto dto) {
        boolean success = userService.updateUserInfo(userId, dto);

        int httpStatus = HttpStatus.OK.value();
        String message = "유저 정보 수정 성공";
        return new SuccessApiResponse<>(httpStatus, message, success);
    }

    @UserPathDocket
    @ApiDeleteUser
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{userId}")
    public CommonApiResponse deleteUser(@PathVariable Long userId) {
        boolean success = userService.deleteUser(userId);

        int httpStatus = HttpStatus.OK.value();
        String message = "유저 탈퇴 성공";
        return new SuccessApiResponse<>(httpStatus, message, success);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public CommonApiResponse getUserList(
            @RequestParam(required = false, defaultValue = "1") int startPage,
            @RequestParam(required = false, defaultValue = "5") int endPage,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        checkParameterWhenPaging(startPage, endPage, pageSize);

        startPage--;
        endPage--;
        List<List<UserListResponseDto>> result = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            Pageable pageable = PageRequest.of(i, pageSize);
            Page<UserListResponseDto> page = userService.getUserList(pageable);
            result.add(page.getContent());
        }

        int httpStatus = HttpStatus.OK.value();
        String message = "회원 목록 조회 성공";
        return new SuccessApiResponse<>(httpStatus, message, result);
    }

    private void checkParameterWhenPaging(int startPage, int endPage, int pageSize) {
        checkPositive(startPage);
        checkPositive(endPage);
        checkPositive(pageSize);

        if (endPage < startPage) throw new InvalidParameterException("400_2", ValidationDefaultMsgUtil.UserList.INVERSION);
    }

    private void checkPositive(int i) {
        if (i <= 0) throw new InvalidParameterException("400_1", ValidationDefaultMsgUtil.UserList.POSITIVE);
    }
}
