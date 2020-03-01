package com.henu.mall.utils;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

/**
 * @author lv
 * @date 2020-02-28 12:40
 */
public class Base64StrToImageUtil {

    public  static MultipartFile base64MutipartFile(String imgStr){
        try {
            String [] baseStr = imgStr.split(",");
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] b =  new byte[0];
            b = base64Decoder.decodeBuffer(baseStr[1]);
            for(int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return  new BASE64DecodedMultipartFile(b,baseStr[0]) ;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
