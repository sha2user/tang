package com.project.tang.controller;

import com.project.tang.dao.mapper.MsgInfoMapper;
import com.project.tang.dao.mapper.SessionListMapper;
import com.project.tang.dao.mapper.UserMapper;
import com.project.tang.dao.pojo.MsgInfo;
import com.project.tang.dao.pojo.SessionList;
import com.project.tang.dao.pojo.User;
import com.project.tang.utils.CurPool;
import com.project.tang.utils.JsonUtils;
import com.project.tang.utils.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;

@Component
@ServerEndpoint("/websocket/{userId}/{sessionId}") //设置访问URL
public class WebSocket {
    @Autowired
    private SessionListMapper sessionListMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MsgInfoMapper msgInfoMapper;

    private Session session;

    @OnOpen
    public void onOpen(Session session, @PathParam(value="userId") Long userId, @PathParam(value="sessionId")String sessionId) {
        //↓未读数清0
        if (sessionListMapper == null){
            this.sessionListMapper = (SessionListMapper) SpringContextUtil.getBean("sessionListMapper");
        }
        SessionList sessionList = sessionListMapper.selectById(Long.valueOf(sessionId));
        if(sessionList!= null){
            if(sessionList.getUnReadCount() != 0){
                sessionList.setUnReadCount(0);
                sessionListMapper.updateById(sessionList);
            }
        }
        //↑未读数清0

        this.session = session;
        CurPool.webSockets.put(userId,this);
        List<Object> list = new ArrayList<>();
        list.add(sessionId);
        list.add(session);
        CurPool.sessionPool.put(userId , list);
        System.out.println("【websocket消息】有新的连接，总数为:"+CurPool.webSockets.size());
    }

    @OnMessage
    public void onMessage(String message) {

        String sessionId = this.session.getRequestParameterMap().get("sessionId").get(0);
        if (sessionId == null){
            System.out.println("sessionId 错误");
        }
        // 在这里无法注入Mapper所以使用这种方式注入Mapper
        if (sessionListMapper == null){
            this.sessionListMapper = (SessionListMapper) SpringContextUtil.getBean("sessionListMapper");
        }
        if (userMapper == null){
            this.userMapper = (UserMapper)SpringContextUtil.getBean("userMapper");
        }
        if (msgInfoMapper == null){
            this.msgInfoMapper = (MsgInfoMapper)SpringContextUtil.getBean("msgInfoMapper");
        }
        SessionList sessionList = sessionListMapper.selectById(Long.valueOf(sessionId));
        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setContent(message);
        msgInfo.setCreateDate(System.currentTimeMillis());
        msgInfo.setFromUserId(sessionList.getUserId());
        msgInfo.setToUserId(sessionList.getToUserId());
        // 消息持久化
        msgInfoMapper.addMsgInfo(msgInfo);

        // 判断用户是否在社交中心
        List<Object> list = CurPool.sessionPool.get(sessionList.getToUserId()); //有可能为99999999
        if (list == null || list.isEmpty()){
            // 用户不在社交中心，更新未读数
            sessionListMapper.addUnReadCount(sessionList.getToUserId(),sessionList.getUserId());
        }else{
            // 用户在社交中心,判断是否在聊天页面中（在与别人的聊天页面中也会直接发送msgInfo，需要修改代码）
            String o = list.get(0).toString();
            Long id= sessionListMapper.selectIdByUser(sessionList.getToUserId(), sessionList.getUserId());
            String sid = id.toString();
            if(o.equals("99999999")){
                //不在该聊天页面中，先更新未读数，再发送未读列表消息
                sessionListMapper.addUnReadCount(sessionList.getToUserId(),sessionList.getUserId());
                List<SessionList> sessionLists = sessionListMapper.findByUserId(sessionList.getToUserId());
                for(SessionList sessionList1:sessionLists){
                    String toUserUsername = sessionList1.getToUserUsername();
                    String avatarByUsername = userMapper.getAvatarByUsername(toUserUsername);
                    sessionList1.setToUserAvatar(avatarByUsername);
                }
                sendTextMessage(sessionList.getToUserId() ,JsonUtils.objectToJson(sessionLists));
            }else{
                if("".equals(sid) || "null".equals(sid)){
                    System.out.println("两人未添加好友，无法聊天");
                }else{
                    if(sid.equals(o)){
                        // 会话存在,则补充头像后 发送消息
                        String avatarById = userMapper.getAvatarById(sessionList.getUserId());
                        msgInfo.setToUserAvatar(avatarById);
                        String avatarById1 = userMapper.getAvatarById(sessionList.getToUserId());
                        msgInfo.setFromUserAvatar(avatarById1);
                        sendTextMessage(sessionList.getToUserId(), JsonUtils.objectToJson(msgInfo));
                    }
                }
            }
        }
        System.out.println("【websocket消息】收到客户端消息:"+message);
    }


    @OnClose
    public void onClose() {
        // 断开连接删除用户删除session
        Long userId = Long.valueOf(this.session.getRequestParameterMap().get("userId").get(0));
        System.out.println( CurPool.sessionPool);
        CurPool.sessionPool.remove(userId);
        CurPool.webSockets.remove(userId);
        System.out.println(CurPool.sessionPool);
    }

    public void sendTextMessage(Long userId, String message) {
        Session session = (Session)CurPool.sessionPool.get(userId).get(1);
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
