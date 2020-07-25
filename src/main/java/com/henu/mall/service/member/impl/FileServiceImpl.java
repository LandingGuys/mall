package com.henu.mall.service.member.impl;

import com.henu.mall.exception.FileUploadException;
import com.henu.mall.provider.AliYunProvider;
import com.henu.mall.service.member.FileService;
import com.henu.mall.vo.FileVo;
import com.henu.mall.vo.ResponseVo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author lv
 * @date 2020-02-24 15:32
 */
@Service
public class FileServiceImpl implements FileService {
    @Resource
    private AliYunProvider aliYunProvider;
    @Override
    public ResponseVo upload(MultipartFile file) {
        try {
            String fileName= aliYunProvider.upload(file.getInputStream(),file.getOriginalFilename());
            FileVo fileVo = new FileVo();
            fileVo.setUrl(fileName);
            fileVo.setMsg("上传成功");
            return ResponseVo.success(fileVo);
        } catch (IOException e) {
            throw new FileUploadException();
        }

    }
}
