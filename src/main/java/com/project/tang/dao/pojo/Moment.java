package com.project.tang.dao.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class Moment {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String title;
    private String content;
    private Long createDate;
    private String username;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    private Integer commentNumber;
    private Integer nice;
}
