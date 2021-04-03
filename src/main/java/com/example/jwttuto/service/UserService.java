package com.example.jwttuto.service;

import com.example.jwttuto.dto.User1Dto;
import com.example.jwttuto.entity.Authority;
import com.example.jwttuto.entity.User1;
import com.example.jwttuto.repository.User1Repository;
import com.example.jwttuto.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    private final User1Repository user1Repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(User1Repository user1Repository, PasswordEncoder passwordEncoder) {
        this.user1Repository = user1Repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User1 signup(User1Dto userDto) { // 회원가입 로직을 수행하는 메소드
        if (user1Repository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build(); // 권한정보 만들기

        User1 user = User1.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build(); // 유저정보 만들기

        return user1Repository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User1> getUserWithAuthorities(String username) {
        return user1Repository.findOneWithAuthoritiesByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User1> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(user1Repository::findOneWithAuthoritiesByUsername);
    } // securityContext에 저장된 유저정보를 받아옴
}
