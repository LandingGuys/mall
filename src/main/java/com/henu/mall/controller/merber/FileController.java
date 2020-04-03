package com.henu.mall.controller.merber;

import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.request.Base64UploadRequest;
import com.henu.mall.service.member.FileService;
import com.henu.mall.utils.BASE64DecodedMultipartFile;
import com.henu.mall.utils.Base64StrToImageUtil;
import com.henu.mall.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-02-23 20:25
 */
@Api(description = "文件上传服务接口")
@RestController
public class FileController {
    @Resource
    private FileService fileService;

    @ApiOperation("file格式上传")
    @PostMapping("/file/upload")
    public ResponseVo upload(MultipartFile file){
        return fileService.upload(file);
    }

    @ApiOperation("base64格式上传")
    @PostMapping("/file/baseUpload")
    public ResponseVo baseUpload(@RequestBody Base64UploadRequest request) {
        try {
            BASE64DecodedMultipartFile base64DecodedMultipartFile = null;
            if (null != request.getImgData() && !request.getImgData().isEmpty()) {
                base64DecodedMultipartFile = (BASE64DecodedMultipartFile) Base64StrToImageUtil.base64MutipartFile(request.getImgData());
            }
            if (null != base64DecodedMultipartFile && !base64DecodedMultipartFile.equals("")) {
                return fileService.upload(base64DecodedMultipartFile);
            }
        } catch (Exception ex) {
            return ResponseVo.error(ResponseEnum.FILE_UPLOAD_ERROR);
        }
        return null;
    }
}
