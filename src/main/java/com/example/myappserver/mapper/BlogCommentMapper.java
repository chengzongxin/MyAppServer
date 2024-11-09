package com.example.myappserver.mapper;

import com.example.myappserver.model.BlogComment;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface BlogCommentMapper {
    
    @Select("SELECT * FROM blog_comment WHERE post_id = #{postId} AND status = 1")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "user", column = "user_id",
                one = @One(select = "com.example.myappserver.mapper.UserMapper.findById"))
    })
    List<BlogComment> findByPostId(@Param("postId") Integer postId);
    
    @Select("SELECT * FROM blog_comment WHERE id = #{id} AND status = 1")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "user", column = "user_id",
                one = @One(select = "com.example.myappserver.mapper.UserMapper.findById"))
    })
    BlogComment findById(@Param("id") Integer id);
    
    @Insert("INSERT INTO blog_comment(post_id, user_id, content, parent_id, status, created_at) " +
            "VALUES(#{postId}, #{userId}, #{content}, #{parentId}, 1, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(BlogComment comment);
    
    @Update("UPDATE blog_comment SET status = 0 WHERE id = #{id}")
    int delete(@Param("id") Integer id);
    
    @Select("SELECT * FROM blog_comment WHERE parent_id = #{parentId} AND status = 1")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "user", column = "user_id",
                one = @One(select = "com.example.myappserver.mapper.UserMapper.findById"))
    })
    List<BlogComment> findReplies(@Param("parentId") Integer parentId);
    
    @Select("SELECT COUNT(*) FROM blog_comment WHERE post_id = #{postId} AND status = 1")
    int countByPostId(@Param("postId") Integer postId);
} 