package com.bridge.mapper;

import com.bridge.entity.Game;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
public interface GameMapper {

    @Select("SELECT * FROM game")
    List<Game> getList();

    @Select("SELECT * FROM game WHERE id = #{gameId}")
    Optional<Game> getGameById(Long gameId);

    void insertGame(Game game);
}
