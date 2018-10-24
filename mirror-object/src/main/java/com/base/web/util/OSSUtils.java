package com.base.web.util;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.base.web.bean.po.resource.Resources;
import com.base.web.bean.po.user.User;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.resource.ResourcesService;
import org.apache.commons.codec.digest.DigestUtils;
import org.hsweb.commons.DateTimeUtils;
import org.hsweb.commons.MD5;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 功能描述: 使用阿里云--对象存储OSS 存储文件
 *
 * @author FQ
 * @date 10/23/2018 2:20 PM
 */

@Service
public class OSSUtils {

    @Resource
    private ResourcesService resourcesService;

    // Endpoint以杭州为例，其它Region请按实际情况填写。
    private String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    private String accessKeyId = "LTAITf5HQzpxZWo0";
    private String accessKeySecret = "EA4DpnH6zzoK3uzwbecV7g9rvbApDz";
    private String bucketName = "kfangq";    //存储空间名
    private Integer expiryTime = 3600 * 1000 * 24; //链接访问过期时间

    /**
     * 功能描述: 创建存储空间
     */
    public void createBucket() {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);    // 创建OSSClient实例。
        ossClient.createBucket(bucketName); // 创建存储空间。
        ossClient.shutdown(); // 关闭OSSClient。
    }

    /**
     * 功能描述: OSS 上传文件流,并返回资源表t_resources记录
     *
     * @param files
     * @return
     */
    public Resources uploadFile(MultipartFile files) {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);    // 创建OSSClient实例。
        String fileAbsName;
        //文件存储的相对路径，以日期分隔，每天创建一个新的目录
        String filePath = "file/".concat(DateTimeUtils.format(new Date(), DateTimeUtils.YEAR_MONTH_DAY)).concat("/");
        String md5 = null;
        String fileType = files.getOriginalFilename().split("[.]")[1];
        try {
            //获取文件的md5值
            md5 = DigestUtils.md5Hex(files.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //判断文件是否已经存在
        Resources resources = resourcesService.selectByMd5(md5);
        if (resources != null) {
            ossClient.shutdown();
            return resources;
        } else {
            //文件存储的相对路径+md5文件名
            fileAbsName = filePath.concat(md5).concat(".").concat(fileType);
        }

        //判断文件类型
        if ("mp4".equals(fileType)) {
            resources.setType("file");
        } else {
            resources.setType("image");
        }

        resources = new Resources();
        resources.setPath(filePath);
        resources.setMd5(md5);
        resources.setSize(files.getSize());
        resources.setName(files.getOriginalFilename());

        try {
            User user = WebUtil.getLoginUser();
            //判断创建用户
            if (user != null) {
                resources.setCreateId(user.getId());
            } else {
                resources.setCreateId(00001L);
            }
            resourcesService.insert(resources);
            // OSS 上传文件流
            InputStream inputStream = files.getInputStream();
            ossClient.putObject(bucketName, fileAbsName, inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OSSException oe) {
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            System.out.println("=====================>>文件上传至阿里云失败！");
            oe.printStackTrace();
        } catch (IOException e) {
            resources.setCreateId(00001L);
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }

        return resources;
    }

    /**
     * 功能描述: 下载到本地文件
     *
     * @param objectName 存储文件名
     * @param localFile  上传文件名
     */
    public void downloadFile(String objectName, String localFile) {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);    // 创建OSSClient实例。
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(localFile));
        // 关闭OSSClient。
        ossClient.shutdown();

    }

    /**
     * 使用签名URL进行临时授权:必须至少包含Signature、Expires和OSSAccessKeyId三个参数。
     *
     * @param resources 资源ID
     * @param type      资源类型
     * @return
     */
    public String getUrl(Map resources, String type) {
        String path = resources.get("path").toString().concat(resources.get("md5").toString()).concat(type);
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);    // 创建OSSClient实例。
        // 设置URL过期时间为1小时。
        Date expiration = new Date(new Date().getTime() + expiryTime);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        String url = ossClient.generatePresignedUrl(bucketName, path, expiration).toString();
        // 关闭OSSClient。
        ossClient.shutdown();
        return url;

    }


}
