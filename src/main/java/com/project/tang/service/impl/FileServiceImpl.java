package com.project.tang.service.impl;

import com.project.tang.dao.mapper.FileMapper;
import com.project.tang.service.FileService;
import com.project.tang.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.project.tang.dao.pojo.File;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileMapper fileMapper;
    @Override
    public Result fileUpload(MultipartFile file) throws IOException, SQLException {
        try{
            SerialBlob serialBlob = new SerialBlob(file.getBytes());
            File file1 = new File();
            file1.setCreateDate(System.currentTimeMillis());
            file1.setTitle("测试文件");
            file1.setFile(serialBlob);
            fileMapper.insert(file1);

        }catch (IOException |  SQLException e){
            e.printStackTrace();
        }
        return Result.success(null);


    }
}
