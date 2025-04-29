package com.project.msy.user.service;

import com.project.msy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // ✅ userId 중복 검사
    public boolean isUserIdDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }
}
