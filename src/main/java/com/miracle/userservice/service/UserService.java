package com.miracle.userservice.service;

import com.miracle.userservice.dto.request.UserJoinRequestDto;
import com.miracle.userservice.dto.request.UserLoginRequestDto;
import com.miracle.userservice.dto.request.UserUpdateInfoRequestDto;
import com.miracle.userservice.dto.response.UserBaseInfoResponseDto;
import com.miracle.userservice.entity.User;
import com.miracle.userservice.exception.DuplicateEmailException;
import com.miracle.userservice.exception.InvalidEmailException;

import java.util.Optional;

public interface UserService {

    /**
     * 유저의 로그인 정보(이메일, 비밀번호)를 받아서 DB와 일치하는지 확인하는 메서드
     *
     * @param dto 로그인 정보(이메일, 비밀번호)
     * @return 로그인에 성공했을 경우 해당 User 객체를 {@code Optional}로 감싸서 반환
     * @throws NullPointerException {@code dto}가 null일 경우
     * @author hazzokko, chocola
     */
    Optional<User> login(UserLoginRequestDto dto);

    /**
     * 회원 가입 요청을 처리하는 메서드
     * 이메일 중복 체크가 선행되며, 기 등록된 이메일이 아닐 경우 회원 가입이 진행된다.
     * 이미 등록된 이메일인 경우 {@code DuplicateEmailException}이 발생한다.
     *
     * @param dto 회원 가입 정보
     * @throws NullPointerException    {@code dto}가 null일 경우
     * @throws DuplicateEmailException 기 등록된 이메일인 경우
     * @author chocola
     */
    void join(UserJoinRequestDto dto);

    /**
     * 이메일 중복 확인을 처리하는 메서드
     * 해당 이메일이 DB에 저장되어 있는지 확인한다. 저장되어 있다면 true, 그렇지 않으면 false를 반환한다.
     *
     * @param email 유저 이메일
     * @return {@code email}이 기 저장되어 있다면 true, 그렇지 않으면 false를 반환
     * @throws InvalidEmailException {@code email}이 올바른 형식이 아닐 경우
     * @author chocola
     */
    boolean checkDuplicate(String email);

    /**
     * 특정 유저의 기본 정보를 반환하는 메서드
     * 기본 정보에는 이메일, 이름, 전화번호, 생년월일, 주소를 포함한다.
     *
     * @param id 유저의 ID
     * @return 유저의 기본 정보가 담긴 DTO(email, name, phone, birth, address)
     * @author chocola
     */
    UserBaseInfoResponseDto getUserBaseInfo(Long id);

    /**
     * 특정 유저의 정보를 수정하는 메서드
     *
     * @param userId 유저의 ID
     * @param dto 유저 정보 수정 데이터
     * @return true
     * @author chocola
     */
    boolean updateUserInfo(Long userId, UserUpdateInfoRequestDto dto);
}
