package com.project.tang.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.tang.dao.pojo.Moment;
import org.springframework.stereotype.Repository;

@Repository
public interface MomentMapper extends BaseMapper<Moment> {
    void addCommentCounts(Long id);
    void subCommentCounts(Long id);
    void addNice(Long id);
    void reduceNice(Long id);
}
