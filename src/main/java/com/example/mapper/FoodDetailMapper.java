package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.FoodDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 食品详情Mapper接口
 */
@Mapper
public interface FoodDetailMapper extends BaseMapper<FoodDetail> {
    
    /**
     * 根据商户ID查询食品详情
     */
    @Select("SELECT * FROM food_detail WHERE merchant_id = #{merchantId} AND status = 'ACTIVE' ORDER BY is_featured DESC, created_at DESC")
    List<FoodDetail> findByMerchantId(@Param("merchantId") Long merchantId);
    
    /**
     * 根据分类查询食品详情
     */
    @Select("SELECT * FROM food_detail WHERE category = #{category} AND status = 'ACTIVE' ORDER BY is_featured DESC, rating DESC")
    List<FoodDetail> findByCategory(@Param("category") String category);
    
    /**
     * 查询推荐食品
     */
    @Select("SELECT * FROM food_detail WHERE is_featured = 1 AND status = 'ACTIVE' ORDER BY rating DESC, created_at DESC LIMIT #{limit}")
    List<FoodDetail> findFeaturedFoods(@Param("limit") Integer limit);
    
    /**
     * 根据标签查询食品详情
     */
    @Select("SELECT * FROM food_detail WHERE tags LIKE CONCAT('%', #{tag}, '%') AND status = 'ACTIVE' ORDER BY rating DESC")
    List<FoodDetail> findByTag(@Param("tag") String tag);
    
    /**
     * 增加浏览次数
     */
    @Update("UPDATE food_detail SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(@Param("id") Long id);
    
    /**
     * 增加点赞次数
     */
    @Update("UPDATE food_detail SET like_count = like_count + 1 WHERE id = #{id}")
    int incrementLikeCount(@Param("id") Long id);
    
    /**
     * 搜索食品详情
     */
    @Select("SELECT * FROM food_detail WHERE (name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%') OR culture_story LIKE CONCAT('%', #{keyword}, '%')) AND status = 'ACTIVE' ORDER BY rating DESC")
    List<FoodDetail> searchFoods(@Param("keyword") String keyword);
}
