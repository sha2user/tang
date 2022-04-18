package com.project.tang.service;

import com.project.tang.vo.Result;
import com.project.tang.vo.params.LikeParam;
import com.project.tang.vo.params.MomentParam;
import com.project.tang.vo.params.MomentUpdate;
import com.project.tang.vo.params.PageParamSecond;

public interface MomentService {
    Result getMomentList();
    Result getMoComment(String sid);
    Result removeCommentById(String sid);
    Result getHotComList();
    Result getLikeMoList();
    Result addArticle(MomentParam momentParam);
    Result removeMoment(String sid);
    Result getCurrent(PageParamSecond pageParamSecond);
    Result updateMoment(MomentUpdate momentUpdate);
    Result selectMoment(String keyWord);
    Result selectMyMoment(String username);
    Result selectMyComment(String username);
    Result selectMyLike(String username);


}
