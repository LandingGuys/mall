package com.henu.mall.provider;

import com.aliyun.oss.OSSClient;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.henu.mall.exception.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AliYunProvider {
    // Endpoint以杭州为例，其它Region请按实际情况填写。
    @Value("${endpoint}")
    private String endpoint;
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    @Value("${accessKeyId}")
    private String accessKeyId;
    @Value("${accessKeySecret}")
    private String accessKeySecret;
    @Value("${bucketName}")
    private String bucketName;
    @Value("${ZmAccessKeyId}")
    private String ZmAccessKeyId;
    @Value("${ZmAccessKeySecret}")
    private String ZmAccessKeySecret;
    @Value("${SingName}")
    private String SingName;
    @Value("${TemplateCode}")
    private String TemplateCode;

    public String upload(InputStream fileStream,String fileName){
        String finalFileName;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyMMdd");
        String[] filePaths=fileName.split("\\.");
        if(filePaths.length>1){
            finalFileName=System.currentTimeMillis()+filePaths[0]+"."+filePaths[filePaths.length-1];
        }else{
            throw new FileUploadException();
        }
        String objectName=sdf.format(new Date())+"/"+finalFileName;
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        if (ossClient.doesBucketExist(bucketName)) {
            System.out.println("您已经创建Bucket：" + bucketName + "。");
        } else {
            System.out.println("您的Bucket不存在，创建Bucket：" + bucketName + "。");
            ossClient.createBucket(bucketName);
        }
        ossClient.putObject(bucketName, objectName, fileStream);
        
        Date expiration =new Date(new Date().getTime() + 3600L * 1000*24*365*10);
        URL url=ossClient.generatePresignedUrl(bucketName,objectName,expiration);
        ossClient.shutdown();
        return url.toString();
    }

    public String sendSms(String phone){
        String code = String.valueOf((int)((Math.random()*9+1)*100000));
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",ZmAccessKeyId,ZmAccessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", SingName);
        request.putQueryParameter("TemplateCode",TemplateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            //System.out.println(response.getData());
            return code;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }
}
