package com.project.tang.utils;

import java.util.UUID;

public class UuidUtils {
    public static String creatUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

