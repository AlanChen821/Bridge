package com.bridge.mapper;

import com.bridge.entity.user.Player;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PlayerMapper {

    @Insert("INSERT INTO player (account, password, name, type) VALUES" +
            " (#{account}, #{password}, #{name}, #{type})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertPlayer(Player player);

    @Select("SELECT * FROM player WHERE account = #{account}")
    Optional<Player> findPlayer(String account);

    @Select(" SELECT " +
            " * " +
            " FROM player " +
            " WHERE account = #{account} " +
            " AND type = #{type} ")
    Player searchPlayer(@Param("account") String account,
                        @Param("type") Integer type);

    List<Player> searchPlayers(@Param("id") Long id,
                               @Param("account") String account,
                               @Param("name") String name,
                               @Param("type") Integer type
    );
}
