package com.project.tang.service;

import com.project.tang.vo.Result;
import com.project.tang.vo.params.OkFriendParam;

public interface SessionListService {
    Result getFriList(String sid);
    Result getInfList(String sid);
    Result okFriend(OkFriendParam okFriendParam);
    Result getReqList(String sid);
    Result noFriend(String sid);
    Result addFriend(String sid, String toUserUsername);
}
