package com.bridge.service;

import com.bridge.entity.Call;
import com.bridge.entity.Game;

import java.util.List;

public interface ICallService {
    Game call(Call call) throws Exception;
}
