package com.bridge.service;

import com.bridge.entity.Game;
import com.bridge.entity.user.Player;

public interface ILoginService {

    Game loginAsGuest(Player player);
}
