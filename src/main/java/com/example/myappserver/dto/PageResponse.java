package com.example.myappserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "分页响应")
public class PageResponse<T> {
    @Schema(description = "当前页数据")
    private List<T> records;
    
    @Schema(description = "总记录数")
    private long total;
    
    @Schema(description = "当前页码")
    private int current;
    
    @Schema(description = "每页大小")
    private int size;
    
    @Schema(description = "总页数")
    private int pages;
    
    public static <T> PageResponse<T> of(List<T> records, long total, int current, int size) {
        PageResponse<T> response = new PageResponse<>();
        response.setRecords(records);
        response.setTotal(total);
        response.setCurrent(current);
        response.setSize(size);
        response.setPages((int) Math.ceil((double) total / size));
        return response;
    }
} 