package com.base.web.service.impl.resource;

import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.bean.po.resource.FileRef;
import com.base.web.bean.po.resource.Resources;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.resource.FileRefMapper;
import com.base.web.service.impl.AbstractServiceImpl;
import com.base.web.service.resource.FileRefService;
import com.base.web.service.resource.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Service("FileRefService")
public class FileRefServiceImpl extends AbstractServiceImpl<FileRef, Long> implements FileRefService {

    @Resource
    protected FileRefMapper fileRefMapper;

    @Resource
    protected FileService fileService;

    //默认数据映射接口
    @Override
    protected FileRefMapper getMapper() {
        return this.fileRefMapper;
    }

    @Override
    public int shopUpdateFileRef(FileRef fileRef) {
        return getMapper().shopUpdateFileRef(fileRef);
    }

    /**
     *
     * @param queryParam  包含属性recordId  资源关联id（必传值）;
     *                    dataType  查询该类型资源时为必传值----（1营业执照id，2，商品详情图片id，3商品轮播id,4：店铺LOGO，5：店铺图片，6商品品论图片-）;
     *                    type 查询视频广告类资源时为必传值------（类型： 0 视频图片，1 视频，2是广告视频，3是广告图片）
     * @param req
     * @return
     */
    @Override
    public List<Map> queryResourceByRecordId(QueryParam queryParam , HttpServletRequest req) {
        List<Map> resourceUrlList = getMapper().queryResourceByRecordId(queryParam);
        /**
         * 判断是否有数据
         * && 查询时传参为recordId  资源关联id
         * 和 type 查询视频广告类资源时为必传值（类型： 0 视频图片，1 视频，2是广告视频，3是广告图片）
         *
         * type == 1 查询视频及其图片
         *
         *
         * type == 2 查询视频广告及其对应的图片
         */
        if(resourceUrlList.size() > 0) {
            if ( queryParam.getParam().get("type") != (null)){
                for(Map resourceUrl : resourceUrlList){
                    // 资源图片判断条件
                    if(resourceUrl.get("type").toString().equals("0") || resourceUrl.get("type").toString().equals("3")){
                        // 资源图片
                        resourceUrl.put("resourceUrl",resourceBuildPath(req,resourceUrl.get("resourceId").toString().trim()));
                    }
                    // 资源视频判断条件
                    if (resourceUrl.get("type").toString().equals("1") || resourceUrl.get("type").toString().equals("2")){
                        String type = ".MP4";
                        // 资源视频
                        resourceUrl.put("resourceUrl",resourceBuildPath(req, String.valueOf(resourceUrl.get("resourceId")), type));
                    }
                }

            }
            /**
             * 判断是否有数据
             * && 查询时传参为recordId  资源关联id
             * 和  dataType  查询该类型资源时为必传值----（1营业执照id，2，商品详情图片id，3商品轮播id,4：店铺LOGO，5：店铺图片，
             * 6商品品论图片,7拒绝退款图片8,申请退款图片-）;
             */

            if( queryParam.getParam().get("dataType") != null){
                for(Map resourceUrl : resourceUrlList){
                    // 资源图片
                    resourceUrl.put("resourceUrl",resourceBuildPath(req,resourceUrl.get("resourceId").toString().trim()));
                }
            }
        }


        return resourceUrlList;
    }

    public static String resourceBuildPath(HttpServletRequest req, String md5) {
        StringBuffer sb = new StringBuffer();
        sb.append(WebUtil.getBasePath(req)).append("file/image/").append(md5).append(".jpg");
        return sb.toString();
    }

    public static String resourceBuildPath(HttpServletRequest req, String md5, String type) {
        StringBuffer sb = new StringBuffer();
        sb.append(WebUtil.getBasePath(req)).append("file/download/").append(md5).append(type);
        return sb.toString();
    }
    //遍历List数据根据其中的图片ID获取对应的资源ID
    public List<Map> addImages(List<Map> list, Integer dataType, HttpServletRequest req) {
        QueryParam param = new QueryParam();
        param.getParam().put("dataType", dataType);
        for (int i = 0; i < list.size(); i++) {
            param.getParam().put("recordId", list.get(i).get("imageSrc"));
            List<Map> imgs = queryResourceByRecordId(param, req);
            if (imgs != null && imgs.size() > 0) {
                list.get(i).put("imageSrc", imgs);
            }
        }
        return list;
    }
    //遍历查询的数据更具视频ID转换为视频resourceid和视频图片resoucreid
    public List<Map> addVideos(List<Map> list, HttpServletRequest req) {
        QueryParam param = new QueryParam();
        //查询视频和视频图片
        for (int i = 0; i < list.size(); i++) {
            param.getParam().put("type", 1);
            param.getParam().put("recordId", list.get(i).get("videoSrc"));
            List<Map> imgs = queryResourceByRecordId(param, req);
            if (imgs != null && imgs.size() > 0) {
                for (int j = 0; j < imgs.size(); j++) {
                    if(imgs.get(j).get("type").toString().equals("0")){
                        list.get(i).put("videoImageSrc", imgs.get(j));
                    }
                    if(imgs.get(j).get("type").toString().equals("1")){
                        list.get(i).put("videoSrc", imgs.get(j));
                    }
                }
            }
        }
        return list;
    }


    //添加前端传来的图片并返回RefId
    // dataType 1营业执照id，2，商品详情图片id，3商品轮播id,4：店铺LOGO，5：店铺图片，
    //         * 6商品品论图片,7拒绝退款图片8,申请退款图片
    public Long addImagesreturnRefId(MultipartFile[] files, Integer dateType) {
        FileRef fileRef = new FileRef();
        Long refId = GenericPo.createUID();
        fileRef.setRefId(refId);
        fileRef.setDataType(dateType);
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                Resources resources = null;
                try {
                    resources = fileService.saveFile(file.getInputStream(), fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileRef.setResourceId(resources.getId());
                fileRef.setId(GenericPo.createUID());
                insert(fileRef);
            }
        }
        return refId;
    }

    //添加前端传来的资源ID并返回RefId
    // dataType 1营业执照id，2，商品详情图片id，3商品轮播id,4：店铺LOGO，5：店铺图片，
    //         * 6商品品论图片,7拒绝退款图片8,申请退款图片
    public Long addImageRefId(Long[] files, Integer dateType) {
        if(files.length == 0){
            return null;
        }
        FileRef fileRef = new FileRef();
        Long refId = GenericPo.createUID();
        fileRef.setRefId(refId);
        fileRef.setDataType(dateType);
        for (int i = 0; i < files.length; i++) {
            fileRef.setResourceId(files[i]);
            fileRef.setId(GenericPo.createUID());
            insert(fileRef);
        }
        return refId;
    }

    @Override
    public List<FileRef> queryByRefId(QueryParam queryParam){
        return getMapper().queryByRefId(queryParam);
    }


    @Override
    public List<Map> queryTypeByRefId(QueryParam queryParam,HttpServletRequest req){
        List<Map> resourceUrlList = getMapper().queryTypeByRefId(queryParam);
        if(resourceUrlList.size() > 0) {
            for (Map resourceUrl : resourceUrlList) {
                // 资源图片判断条件
                if (resourceUrl.get("type").toString().equals("3")) {
                    // 资源图片
                    resourceUrl.put("resourceUrl", resourceBuildPath(req, resourceUrl.get("resourceId").toString().trim()));
                }
                // 资源视频判断条件
                if (resourceUrl.get("type").toString().equals("2")) {
                    String type = ".MP4";
                    // 资源视频
                    resourceUrl.put("resourceUrl", resourceBuildPath(req, String.valueOf(resourceUrl.get("resourceId")), type));
                }
            }

        }
        return resourceUrlList;

    }
}
