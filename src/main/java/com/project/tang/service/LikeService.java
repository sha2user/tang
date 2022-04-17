package com.project.tang.service;

import com.project.tang.vo.Result;
import com.project.tang.vo.params.LikeParam;

public interface LikeService {
    Boolean getLikeStatus(Long momentId,Long userId);
    Result addNice(LikeParam likeParam);
    Result reduceNice(LikeParam likeParam);
    void setLike(Long momentId,Long userId);
    void deleteLike(Long momentId,Long userId);
}
