package com.hororok.monta.service;

import com.hororok.monta.dto.request.PostRegisterRequestDto;
import com.hororok.monta.dto.request.PostLoginRequestDto;
import com.hororok.monta.dto.response.RegisterResponseDto;
import com.hororok.monta.dto.response.FailResponseDto;
import com.hororok.monta.dto.response.PostLoginResponseDto;
import com.hororok.monta.entity.Account;
import com.hororok.monta.jwt.JwtFilter;
import com.hororok.monta.jwt.TokenProvider;
import com.hororok.monta.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    @Autowired
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @Transactional
    public ResponseEntity<?> postRegister(PostRegisterRequestDto postRegisterRequestDto) {

        // 이메일 중복 여부 점검
        if(accountRepository.existsByEmail(postRegisterRequestDto.getEmail())) {
            List<String> errors = new ArrayList<>();
            errors.add("이미 사용중인 이메일 입니다. 다른 이메일을 사용해주세요."); // 중복된 이메일에 대한 에러 메시지를 Map에 추가

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new FailResponseDto(HttpStatus.CONFLICT.value(), "해당 이메일 사용 불가", errors));
        }

        // 비밀 번호 encode
        PostRegisterRequestDto encodeDto = PostRegisterRequestDto.builder()
                .email(postRegisterRequestDto.getEmail())
                .password(passwordEncoder.encode(postRegisterRequestDto.getPassword()))
                .name(postRegisterRequestDto.getName())
                .build();

        // DB에 저장
        Account saveMember = accountRepository.save(new Account(encodeDto));
        return ResponseEntity.ok(new RegisterResponseDto(saveMember.getId()));
    }

    @Transactional
    public ResponseEntity<?> postLogin(PostLoginRequestDto postLoginRequestDto) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(postLoginRequestDto.getEmail(), postLoginRequestDto.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.createToken(authentication);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

            return ResponseEntity.ok(new PostLoginResponseDto(jwt));
    }
}
