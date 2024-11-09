package com.example.myappserver.mapper;

import com.example.myappserver.model.BlogTag;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface BlogTagMapper {
    
    @Select("SELECT * FROM blog_tag")
    List<BlogTag> findAll();
    
    @Select("SELECT * FROM blog_tag WHERE id = #{id}")
    BlogTag findById(@Param("id") Integer id);
    
    @Select("SELECT * FROM blog_tag WHERE name = #{name}")
    BlogTag findByName(@Param("name") String name);
    
    @Insert("INSERT INTO blog_tag(name, created_at) VALUES(#{name}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(BlogTag tag);
    
    @Delete("DELETE FROM blog_tag WHERE id = #{id}")
    int delete(@Param("id") Integer id);
    
    @Select("SELECT t.* FROM blog_tag t " +
            "JOIN blog_post_tag pt ON t.id = pt.tag_id " +
            "WHERE pt.post_id = #{postId}")
    List<BlogTag> findByPostId(@Param("postId") Integer postId);
} 