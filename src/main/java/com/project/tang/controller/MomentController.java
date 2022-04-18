package com.project.tang.controller;

import com.project.tang.service.LikeService;
import com.project.tang.service.MomentCommentService;
import com.project.tang.service.MomentService;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("moment")
public class MomentController {
    @Autowired
    private MomentService momentService;
    @Autowired
    private MomentCommentService momentCommentService;
    @Autowired
    private LikeService likeService;

    @GetMapping("momentList")
    public Result getMomentList(){
        return momentService.getMomentList();
    }

    @GetMapping("moComment/{id}")
    public Result getMoComment(@PathVariable("id")String sid){
        return momentService.getMoComment(sid);
    }

    @GetMapping("removeComment/{id}")
    public Result removeCommentById(@PathVariable("id")String sid){
        return momentService.removeCommentById(sid);
    }
    @PostMapping("submitComment")
    public Result submitComment(@RequestBody MomentCommentParam momentCommentParam){
        return momentCommentService.submitComment(momentCommentParam);
    }

    @PostMapping("addNice")
    public Result addNice(@RequestBody LikeParam likeParam){

        return likeService.addNice(likeParam);
    }

    @PostMapping("reduceNice")
    public Result reduceNice(@RequestBody LikeParam likeParam){
        return likeService.reduceNice(likeParam);
    }

    @GetMapping("hotComList")
    public Result getHotComList(){
        return momentService.getHotComList();
    }
    @GetMapping("likeMoList")
    public Result getLikeMoList(){
        return momentService.getLikeMoList();
    }

    @PostMapping("addMoment")
    public Result addMoment(@RequestBody MomentParam momentParam){

        return momentService.addArticle(momentParam);
    }

    @GetMapping("removeMoment/{id}")
    public Result removeMoment(@PathVariable("id")String sid){
        return momentService.removeMoment(sid);
    }

    @PostMapping("getLike")
    public Result getLike(@RequestBody LikeParam likeParam){
        Long momentId = Long.valueOf(likeParam.getMomentId());
        Long userId = Long.valueOf(likeParam.getUserId());
        Boolean likeStatus = likeService.getLikeStatus(momentId, userId);
        if(likeStatus){
            return Result.success(likeStatus);
        }else {
            return Result.success(likeStatus);
        }
    }

    @PostMapping("getCurrent")
    public Result getCurrent(@RequestBody PageParamSecond pageParamSecond){
        return momentService.getCurrent(pageParamSecond);
    }

    @PostMapping("updateMoment")
    public Result updateMoment(@RequestBody MomentUpdate momentUpdate){
        return momentService.updateMoment(momentUpdate);
    }

    @GetMapping("selectMoment/{keyWord}")
    public Result selectMoment(@PathVariable("keyWord")String keyWord){
        return momentService.selectMoment(keyWord);
    }

    @GetMapping("selectMyMoment/{username}")
    public Result selectMyMoment(@PathVariable("username")String username){
        return momentService.selectMyMoment(username);
    }

    @GetMapping("selectMyComment/{username}")
    public Result selectMyComment(@PathVariable("username")String username){
        return momentService.selectMyComment(username);
    }

    @GetMapping("selectMyLike/{userId}")
    public Result selectMyLike(@PathVariable("userId")String userId){
        return momentService.selectMyLike(userId);
    }




}
