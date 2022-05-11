package com.project.tang.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.tang.dao.pojo.SessionList;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionListMapper extends BaseMapper<SessionList> {
    void addUnReadCount(Long userId,Long toUserId);
    Long selectIdByUser(Long userId,Long toUserId);
    List<SessionList> findByUserId(Long userId);
}
