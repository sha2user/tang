package com.project.tang.vo.params;

import lombok.Data;

@Data
public class PasswordParam {
    //此处字段的设置 要与前端axios发送的参数名称相同，否则无法匹配
   // private String username;
    private String oldPass;
    private String checkPass;
}
