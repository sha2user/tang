package com.project.tang.utils;

import com.project.tang.controller.WebSocket;
import com.project.tang.dao.pojo.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 统一管理session、websocket、curUser
 */
public class CurPool {

//    public static CopyOnWriteArraySet<WebSocket> webSockets =new CopyOnWriteArraySet<>();
    public static Map<Long, WebSocket> webSockets = new ConcurrentHashMap<>();
    // list 里面第一个存sessionId，第二个存session
    public static Map<Long, List<Object>> sessionPool = new ConcurrentHashMap<>();
    // 当前登录用户x
    //public static Map<String, User> curUserPool = new ConcurrentHashMap<>();
}
