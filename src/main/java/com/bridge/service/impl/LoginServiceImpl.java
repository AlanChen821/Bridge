package com.bridge.service.impl;

import com.bridge.entity.user.Player;
import com.bridge.service.ILoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@Slf4j
public class LoginServiceImpl implements ILoginService {

    @Override
    public String loginAsGuest(Player player) {
        String account = player.getAccount();
        String encodedAccount = Base64.getEncoder().encodeToString(account.getBytes());
        log.info("Player {} has logged in.", player.getAccount());
        //  TODO : find whether there's an empty room already, if there isn't, create a new one.
        //  otherwise, attend to the existing room.
        return encodedAccount;
    }
}
