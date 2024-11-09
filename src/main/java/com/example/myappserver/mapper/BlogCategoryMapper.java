package com.example.myappserver.mapper;

import com.example.myappserver.model.BlogCategory;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface BlogCategoryMapper {
    
    @Select("SELECT * FROM blog_category")
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