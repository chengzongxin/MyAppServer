-- 创建数据库
CREATE DATABASE IF NOT EXISTS mydatabase CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE mydatabase;

-- 创建表（如果不存在）
CREATE TABLE IF NOT EXISTS user (
    -- 用户表结构
);

CREATE TABLE IF NOT EXISTS blog_post (
    -- 博客表结构
);

-- 其他表的创建语句...

-- 添加索引
ALTER TABLE blog_post ADD INDEX idx_author_id (author_id);
ALTER TABLE blog_post ADD INDEX idx_created_at (created_at);
ALTER TABLE blog_post ADD INDEX idx_status (status); 