package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.RouteDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 路线详情Mapper接口
 * 
 * @author example
 * @since 1.0.0
 */
@Mapper
public interface RouteDetailMapper extends BaseMapper<RouteDetail> {

    /**
     * 根据路线ID查询路线详情
     */
    @Select("SELECT * FROM route_details WHERE route_id = #{routeId} AND status = 'ACTIVE'")
    RouteDetail findByRouteId(@Param("routeId") Long routeId);

    /**
     * 根据路线类型查询路线列表
     */
    @Select("SELECT * FROM route_details WHERE route_type = #{routeType} AND status = 'ACTIVE' ORDER BY rating DESC")
    List<RouteDetail> findByRouteType(@Param("routeType") String routeType);

    /**
     * 查询热门路线
     */
    @Select("SELECT * FROM route_details WHERE status = 'ACTIVE' ORDER BY rating DESC, total_distance ASC LIMIT #{limit}")
    List<RouteDetail> findPopularRoutes(@Param("limit") Integer limit);
}
