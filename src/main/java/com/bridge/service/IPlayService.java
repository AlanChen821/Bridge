package com.bridge.service;

import com.bridge.entity.Game;
import com.bridge.entity.Play;

public interface IPlayService {
    Game play(String token, Play play) throws Exception;
}
