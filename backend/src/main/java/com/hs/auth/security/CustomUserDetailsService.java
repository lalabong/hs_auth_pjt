package com.hs.auth.security;

import com.hs.auth.constants.AppConstants;
import com.hs.auth.entity.User;
import com.hs.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(AppConstants.Messages.USER_NOT_FOUND + email));

        return createUserDetails(user);
    }

    // userId로 사용자 정보 로드 (JWT 인증용)
    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new UsernameNotFoundException(AppConstants.Messages.USER_NOT_FOUND + "ID: " + userId));

        return createUserDetails(user);
    }

    // UserDetails 객체 생성 (공통 메서드)
    private UserDetails createUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .build();
    }
}