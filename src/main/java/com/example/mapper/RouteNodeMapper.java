package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.RouteNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 路线节点Mapper接口
 * 
 * @author example
 * @since 1.0.0
 */
@Mapper
public interface RouteNodeMapper extends BaseMapper<RouteNode> {

    /**
     * 根据路线ID查询所有节点
     */
    @Select("SELECT * FROM route_nodes WHERE route_id = #{routeId} AND status = 'ACTIVE' ORDER BY node_order ASC")
    List<RouteNode> findByRouteId(@Param("routeId") Long routeId);

    /**
     * 根据路线ID和节点类型查询节点
     */
    @Select("SELECT * FROM route_nodes WHERE route_id = #{routeId} AND node_type = #{nodeType} AND status = 'ACTIVE' ORDER BY node_order ASC")
    List<RouteNode> findByRouteIdAndNodeType(@Param("routeId") Long routeId, @Param("nodeType") String nodeType);
}
