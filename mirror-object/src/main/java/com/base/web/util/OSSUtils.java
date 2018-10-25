package com.base.web.util;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.resource.Resources;
import com.base.web.bean.po.user.User;
import com.base.web.core.exception.NotFoundException;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.VideoUserMapper;
import com.base.web.service.resource.ResourcesService;
import org.apache.commons.codec.digest.DigestUtils;
import org.hsweb.commons.DateTimeUtils;
import org.hsweb.commons.MD5;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @Resource
    private VideoUserMapper videoUserMapper;

    // Endpoint以杭州为例，其它Region请按实际情况填写。
    private String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    private String accessKeyId = "LTAITf5HQzpxZWo0";
    private String accessKeySecret = "EA4DpnH6zzoK3uzwbecV7g9rvbApDz";
    private String bucketName = "kfangq";    //存储空间名
    private Integer expiryTime = 3600 * 1000 * 24; //链接访问过期时间

    /**
     * 功能描述: OSS 上传文件流,并返回资源表t_resources记录
     *
     * @param files
     * @return
     */
    public Resources uploadFile(MultipartFile files) {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);    // 创建OSSClient实例。
        String fileAbsName;
        String md5 = "";

        //文件存储的相对路径，以日期分隔，每天创建一个新的目录
        String filePath = "file/".concat(DateTimeUtils.format(new Date(), DateTimeUtils.YEAR_MONTH_DAY)).concat("/");
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
        resources = new Resources();

        //判断文件类型
        if ("mp4".equals(fileType)) {
            resources.setType("file");
        } else {
            resources.setType("image");
        }

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

            // OSS 上传文件流
            InputStream inputStream = files.getInputStream();
            ossClient.putObject(bucketName, fileAbsName, inputStream);

            // 判断文件是否存在。
            boolean found = ossClient.doesObjectExist(bucketName, fileAbsName);
            if (found) {
                resourcesService.insert(resources);
            } else {
                throw new NotFoundException("文件上传失败");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OSSException oe) {
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

    public String deleteFile(String yourObjectName) {
        String message = "删除失败";
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);    // 创建OSSClient实例。
        // 判断文件是否存在。
        boolean found = ossClient.doesObjectExist(bucketName, yourObjectName);
        if (found) {
            // 删除文件。
            ossClient.deleteObject(bucketName, yourObjectName);
            message = "删除成功";
        }
        // 关闭OSSClient。
        ossClient.shutdown();
        return message;
    }

    /**
     * 功能描述: 生成资源访问路径
     *
     * @param resources 资源ID
     * @param type      资源类型
     * @return
     */
    public String getUrl(Map resources, String type) {
        //拼接网络资源路径
        String path = resources.get("path").toString().concat(resources.get("md5").toString()).concat(type);
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 设置URL过期时间为1小时。
        Date expiration = new Date(new Date().getTime() + expiryTime);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        String url = ossClient.generatePresignedUrl(bucketName, path, expiration).toString();
        // 关闭OSSClient。
        ossClient.shutdown();
        return url;

    }


    /**
     * 功能描述: 根据资源关联ID拼接生成资源访问路径
     *
     * @param list
     */
    public List<Map> jointUrl(List<Map> list) {
        for (Map map : list) {
            if (map.get("videoSrc") != null) {
                Long recordId = Long.valueOf(map.get("videoSrc").toString());
                //视频对应图片地址
                Map imageMap = videoUserMapper.selectVideoImageUrl(recordId);
                map.put("videoImageUrl", getUrl(imageMap, ".jpg"));
                //视频对应地址
                Map videoMap = videoUserMapper.selectVideoUrl(recordId);
                map.put("videoUrl", getUrl(videoMap, ".mp4"));
            }
        }
        return list;
    }

    /**
     * 功能描述: 单个根据资源关联ID拼接生成视频图片访问路径
     *
     * @param recordId
     * @return
     */
    public String selectVideoImageUrl(String recordId) {
        Map imageMap = videoUserMapper.selectVideoImageUrl(Long.valueOf(recordId));
        return getUrl(imageMap, ".jpg");
    }

    /**
     * 功能描述: 单个根据资源关联ID拼接生成视频访问路径
     *
     * @param recordId
     * @return
     */
    public String selectVideoUrl(String recordId) {
        Map imageMap = videoUserMapper.selectVideoUrl(Long.valueOf(recordId));
        return getUrl(imageMap, ".mp4");
    }


}
