package com.project.msy.user.controller;

import com.project.msy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ 아이디 중복 검사 API
    @GetMapping("/check-userid")
    public boolean checkUserId(@RequestParam String userId) {
        return userService.isUserIdDuplicate(userId);
    }
}