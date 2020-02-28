package com.henu.mall.service;

import com.henu.mall.vo.ResponseVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lv
 * @date 2020-02-24 15:31
 */
public interface FileService {

    ResponseVo upload(MultipartFile file);
}
