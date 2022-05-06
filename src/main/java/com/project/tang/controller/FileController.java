package com.project.tang.controller;

import com.project.tang.dao.pojo.User;
import com.project.tang.service.FileService;
import com.project.tang.service.UserService;
import com.project.tang.utils.QiniuUtils;
import com.project.tang.utils.UuidUtils;
import com.project.tang.vo.LoginUserVo;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.PageParamSecond;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("file")
public class FileController {
    @Autowired
    private UserService userService;
    @Autowired
    private QiniuUtils qiniuUtils;
    @Autowired
    private FileService fileService;

    @RequestMapping("/fileListNum")
    public Result getFileList(){
        return fileService.getFileListNum();
    }

    @PostMapping("getCurrent")
    public Result getCurrent(@RequestBody PageParamSecond pageParamSecond){
        return fileService.getCurrent(pageParamSecond);
    }

    @RequestMapping("/upload")
    public Result FileUpdate (HttpServletRequest request,MultipartFile file) {
        //得到上传时的文件名字
        String originalname=file.getOriginalFilename();
        //截取文件名后缀
        String newName= originalname.substring(originalname.lastIndexOf("."));
        //利用UUidUtil工具类生成新的文件名字
        newName = UuidUtils.creatUUID()+newName;
        //上传到七牛云服务器
        boolean upload = qiniuUtils.upload(file, newName);
        if(upload){
            String fileUrl=QiniuUtils.url+newName;
            String title=request.getParameter("title");
            String username =request.getParameter("username");
            String description = request.getParameter("description");
            boolean b = fileService.uploadFile(title, fileUrl, username, description);
            if(b){
                return Result.success(null);
            }else {
                return Result.fail(2001,"文件数据库更新失败");
            }
        }
        return Result.fail(2001,"上传失败");
    }

    @RequestMapping("/avatar")
    public Result AvatarUpload ( HttpServletRequest request, MultipartFile file) {
        //得到上传时的文件名字
        String originalname=file.getOriginalFilename();
        //截取图片名后缀
        String newName= originalname.substring(originalname.lastIndexOf("."));
        //利用UUidUtil工具类生成新的文件名字
        newName = UuidUtils.creatUUID()+newName;
        //上传到七牛云服务器，按量付费，速度快，降低自身应用服务器的带宽消耗
        boolean upload = qiniuUtils.upload(file, newName);
        if(upload){
            String fileUrl=QiniuUtils.url+newName;
            String sid=request.getParameter("id");
            userService.setAvatarById(sid,fileUrl);
            return Result.success(QiniuUtils.url+newName);
        }
        return Result.fail(2001,"上传失败");
    }
}