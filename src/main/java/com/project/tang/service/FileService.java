package com.project.tang.service;

import com.project.tang.vo.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

public interface FileService {
    Result fileUpload (MultipartFile file) throws IOException, SQLException;
}
