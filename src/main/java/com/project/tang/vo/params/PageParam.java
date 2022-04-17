package com.project.tang.vo.params;

import lombok.Data;

@Data
public class PageParam {
    private String currentPage;
    private String pageSize;
    private String categoryName;
}
