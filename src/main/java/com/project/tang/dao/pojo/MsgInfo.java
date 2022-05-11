package com.project.tang.dao.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class MsgInfo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fromUserId;
    private String fromUserAvatar;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long toUserId;
    private String toUserAvatar;
    private String content;
    private Long createDate;
}
