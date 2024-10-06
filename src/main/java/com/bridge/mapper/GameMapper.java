package com.bridge.mapper;

import com.bridge.entity.Game;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface GameMapper {

    @Select("SELECT * FROM game")
    public List<Game> getList();
}
