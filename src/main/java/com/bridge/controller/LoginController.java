package com.bridge.controller;

import com.bridge.entity.user.Player;
import com.bridge.service.ILoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final ILoginService loginService;

    public LoginController(ILoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/loginAsGuest")
    public String loginAsGuest(@RequestBody Player player) {
        return loginService.loginAsGuest(player);
    }
}
