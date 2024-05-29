package com.bridge.service;

import com.bridge.entity.Game;
import com.bridge.entity.Play;

public interface PlayService {
    Game play(Play play) throws Exception;
}
