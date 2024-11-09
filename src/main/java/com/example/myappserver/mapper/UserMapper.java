package com.example.myappserver.mapper;

import com.example.myappserver.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    
    @Select("SELECT * FROM user")
    List<User> findAll();
    
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(@Param("id") Integer id);
    
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);
    
    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(@Param("email") String email);
    
    @Insert("INSERT INTO user(username, name, email, password, status, created_at, updated_at) " +
            "VALUES(#{username}, #{name}, #{email}, #{password}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
    
    @Update("UPDATE user SET " +
            "name = #{name}, " +
            "email = #{email}, " +
            "status = COALESCE(#{status}, status), " +
            "updated_at = NOW() " +
            "WHERE id = #{id}")
    int update(User user);
    
    @Update("UPDATE user SET last_login_time = NOW() WHERE id = #{id}")
    int updateLoginTime(@Param("id") Integer id);
    
    @Update("UPDATE user SET avatar_url = #{avatarUrl}, updated_at = NOW() WHERE id = #{id}")
    int updateAvatar(@Param("id") Integer id, @Param("avatarUrl") String avatarUrl);
    
    @Delete("DELETE FROM user WHERE id = #{id}")
    int delete(@Param("id") Integer id);
} 