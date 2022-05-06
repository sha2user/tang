package com.project.tang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.tang.dao.mapper.FileMapper;
import com.project.tang.dao.pojo.Moment;
import com.project.tang.service.FileService;
import com.project.tang.vo.ErrorCode;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.PageParamSecond;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.project.tang.dao.pojo.File;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileMapper fileMapper;

    @Override
    public Result getFileListNum() {
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        Integer integer = fileMapper.selectCount(queryWrapper);
        return Result.success(integer);
    }

    @Override
    public Result getCurrent(PageParamSecond pageParamSecond) {
        int currentPage=Integer.parseInt(pageParamSecond.getCurrentPage());
        int pageSize=Integer.parseInt(pageParamSecond.getPageSize());
        if( (currentPage==0) || (pageSize==0)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        QueryWrapper<File> queryWrapper=new QueryWrapper<>();
        Page<File> page=new Page<>(currentPage,pageSize);
        queryWrapper.orderByDesc("create_date");
        Page<File> filePage = fileMapper.selectPage(page, queryWrapper);
        List<File> list=filePage.getRecords();
        return Result.success(list);
    }

    @Override
    public boolean uploadFile(String title, String fileUrl, String username, String description) {
        File file = new File();
        file.setTitle(title);
        file.setCreateDate(System.currentTimeMillis());
        file.setDescription(description);
        file.setUsername(username);
        file.setUrl(fileUrl);
        int insert = fileMapper.insert(file);
        if(insert!=0){
            return true;
        }else {
            return false;
        }

    }
}
