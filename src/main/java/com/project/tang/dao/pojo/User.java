package com.project.tang.dao.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class User {
    //username password admin create_date phone_number name salt
    // 以后 用户多了之后，要进行分表操作，id就需要用分布式id了
    //MybatisPlus id默认使用雪花算法
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String username;
    private String password;
    private Integer admin;
    private Long createDate;
    private String phoneNumber;
    private String name;
    private String salt;
    private String token;
}
