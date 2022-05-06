package com.project.tang.service;

import com.project.tang.vo.Result;
import com.project.tang.vo.params.PageParamSecond;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

public interface FileService {
    Result getFileListNum();
    Result getCurrent(PageParamSecond pageParamSecond);
    boolean uploadFile(String title, String fileUrl, String username, String description);
}
