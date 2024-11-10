package com.example.myappserver.mapper;

import com.example.myappserver.model.BlogCategory;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface BlogCategoryMapper {
    
    @Select("SELECT c.*, COUNT(DISTINCT CASE WHEN p.status != 2 THEN pc.post_id END) as post_count " +
            "FROM blog_category c " +
            "LEFT JOIN blog_post_category pc ON c.id = pc.category_id " +
            "LEFT JOIN blog_post p ON pc.post_id = p.id " +
            "GROUP BY c.id")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "description", column = "description"),
        @Result(property = "postCount", column = "post_count"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<BlogCategory> findAll();
    
    @Select("SELECT * FROM blog_category WHERE id = #{id}")
    BlogCategory findById(@Param("id") Integer id);
    
    @Insert("INSERT INTO blog_category(name, description, created_at, updated_at) " +
            "VALUES(#{name}, #{description}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(BlogCategory category);
    
    @Update("UPDATE blog_category SET name = #{name}, description = #{description}, " +
            "updated_at = NOW() WHERE id = #{id}")
    int update(BlogCategory category);
    
    @Delete("DELETE FROM blog_category WHERE id = #{id}")
    int delete(@Param("id") Integer id);
} 