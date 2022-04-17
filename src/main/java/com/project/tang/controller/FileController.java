package com.project.tang.controller;

import com.project.tang.service.FileService;
import com.project.tang.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

@RestController

public class FileController {
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Result fileUpload(HttpServletRequest request) throws IOException, SQLException {

        StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();

        MultipartHttpServletRequest multipartRequest=resolver.resolveMultipart(request);
        MultipartFile multipartFile = multipartRequest.getFile("file");
        return fileService.fileUpload(multipartFile);
    }
}
