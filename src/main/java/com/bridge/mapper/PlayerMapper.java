package com.bridge.mapper;

import com.bridge.entity.user.Player;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PlayerMapper {

    @Insert("INSERT INTO player (account, password, name, type) VALUES" +
            " (#{account}, #{password}, #{name}, #{type})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertPlayer(Player player);

    Player searchPlayer(@Param("id") Long id,
                        @Param("account") String account,
                        @Param("name") String name
    );
}
