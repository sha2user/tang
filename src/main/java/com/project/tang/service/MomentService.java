package com.project.tang.service;

import com.project.tang.vo.Result;
import com.project.tang.vo.params.LikeParam;
import com.project.tang.vo.params.MomentParam;

public interface MomentService {
    Result getMomentList();
    Result getMoComment(String sid);
    Result removeCommentById(String sid);
    Result getHotComList();
    Result getLikeMoList();
    Result addArticle(MomentParam momentParam);
    Result removeMoment(String sid);
}
