package com.example.myappserver.mapper;

import com.example.myappserver.model.BlogCategory;
import com.example.myappserver.model.BlogPost;
import com.example.myappserver.model.BlogTag;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface BlogPostMapper {
    
    @Select("SELECT * FROM blog_post WHERE status != 2")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "author", column = "author_id",
                one = @One(select = "com.example.myappserver.mapper.UserMapper.findById")),
        @Result(property = "categories", column = "id",
                many = @Many(select = "findCategoriesByPostId")),
        @Result(property = "tags", column = "id",
                many = @Many(select = "findTagsByPostId"))
    })
    List<BlogPost> findAll();
    
    @Select("SELECT * FROM blog_post WHERE id = #{id} AND status != 2")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "author", column = "author_id",
                one = @One(select = "com.example.myappserver.mapper.UserMapper.findById")),
        @Result(property = "categories", column = "id",
                many = @Many(select = "findCategoriesByPostId")),
        @Result(property = "tags", column = "id",
                many = @Many(select = "findTagsByPostId"))
    })
    BlogPost findById(@Param("id") Integer id);
    
    @Insert("INSERT INTO blog_post(title, content, summary, cover_image, author_id, status, " +
            "created_at, updated_at) VALUES(#{title}, #{content}, #{summary}, #{coverImage}, " +
            "#{authorId}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(BlogPost post);
    
    @Update("UPDATE blog_post SET title = #{title}, content = #{content}, " +
            "summary = #{summary}, cover_image = #{coverImage}, status = #{status}, " +
            "updated_at = NOW() WHERE id = #{id}")
    int update(BlogPost post);
    
    @Update("UPDATE blog_post SET status = 2, updated_at = NOW() WHERE id = #{id}")
    int delete(@Param("id") Integer id);
    
    @Update("UPDATE blog_post SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(@Param("id") Integer id);
    
    @Update("UPDATE blog_post SET like_count = like_count + 1 WHERE id = #{id}")
    int incrementLikeCount(@Param("id") Integer id);
    
    @Update("UPDATE blog_post SET comment_count = comment_count + 1 WHERE id = #{id}")
    int incrementCommentCount(@Param("id") Integer id);
    
    @Select("SELECT c.* FROM blog_category c " +
            "JOIN blog_post_category pc ON c.id = pc.category_id " +
            "WHERE pc.post_id = #{postId}")
    List<BlogCategory> findCategoriesByPostId(@Param("postId") Integer postId);
    
    @Select("SELECT t.* FROM blog_tag t " +
            "JOIN blog_post_tag pt ON t.id = pt.tag_id " +
            "WHERE pt.post_id = #{postId}")
    List<BlogTag> findTagsByPostId(@Param("postId") Integer postId);
    
    @Insert("INSERT INTO blog_post_category(post_id, category_id) VALUES(#{postId}, #{categoryId})")
    int insertCategory(@Param("postId") Integer postId, @Param("categoryId") Integer categoryId);
    
    @Insert("INSERT INTO blog_post_tag(post_id, tag_id) VALUES(#{postId}, #{tagId})")
    int insertTag(@Param("postId") Integer postId, @Param("tagId") Integer tagId);
    
    @Delete("DELETE FROM blog_post_category WHERE post_id = #{postId}")
    int deleteCategories(@Param("postId") Integer postId);
    
    @Delete("DELETE FROM blog_post_tag WHERE post_id = #{postId}")
    int deleteTags(@Param("postId") Integer postId);
} 