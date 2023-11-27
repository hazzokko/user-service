package com.miracle.userservice.controller;

import com.miracle.userservice.controller.response.CommonApiResponse;
import com.miracle.userservice.controller.response.SuccessApiResponse;
import com.miracle.userservice.dto.response.ApplicationLetterResponseDto;
import com.miracle.userservice.service.ApplicationLetterService;
import com.miracle.userservice.swagger.ApiGetResumeAndCoverLetterList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/v1/user/{userId}/application-letter")
@RestController
public class ApplicationLetterController {

    private final ApplicationLetterService applicationLetterService;

    @ApiGetResumeAndCoverLetterList
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/popup")
    public CommonApiResponse getResumeAndCoverLetterList(@PathVariable Long userId) {
        ApplicationLetterResponseDto dto = applicationLetterService.getResumeAndCoverLetterList(userId);
        int httpStatus = HttpStatus.OK.value();
        String message = "이력서 및 자기소개서 목록 출력 성공";
        return new SuccessApiResponse<>(httpStatus, message, dto);
    }
}