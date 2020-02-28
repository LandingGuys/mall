package com.henu.mall.controller;

import com.henu.mall.service.FileService;
import com.henu.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-02-23 20:25
 */
@RestController
public class FileController {
    @Resource
    private FileService fileService;


    @PostMapping("/file/upload")
    public ResponseVo upload(MultipartFile file){
        return fileService.upload(file);
    }
}
