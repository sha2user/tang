package com.project.tang.dao.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class File {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private Long createDate;
    private String title;
    private Object file;
}
