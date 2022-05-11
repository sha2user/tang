package com.project.tang.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.tang.dao.pojo.MsgInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MsgInfoMapper extends BaseMapper<MsgInfo> {
    List<MsgInfo> selectMessageList(Long fromUserId,Long toUserId);
    void addMsgInfo(MsgInfo msgInfo);
}
