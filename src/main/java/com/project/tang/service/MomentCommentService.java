package com.project.tang.service;

import com.project.tang.vo.Result;
import com.project.tang.vo.params.MomentCommentParam;

public interface MomentCommentService {
    Result submitComment(MomentCommentParam momentCommentParam);
}
