package com.project.tang.dao.mapper;

import com.project.tang.dao.pojo.Moment;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GetLikeMapper {
    List<Moment> getMyLike(Long id);
}
