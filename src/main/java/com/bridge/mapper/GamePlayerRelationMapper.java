package com.bridge.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GamePlayerRelationMapper {

    void insertGamePlayerRelation(@Param("gameId") Long gameId,
                                  @Param("playerId") Long playerId,
                                  @Param("type") Integer type,
                                  @Param("isMaster") Boolean isMaster);
}
