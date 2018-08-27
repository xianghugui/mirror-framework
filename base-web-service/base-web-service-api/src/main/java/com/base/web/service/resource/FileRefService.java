package com.base.web.service.resource;

import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.resource.FileRef;

import com.base.web.service.GenericService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


public interface FileRefService extends GenericService<FileRef, Long> {


    int shopUpdateFileRef(FileRef fileRef);
    /**
     * 根据资源(商品轮播，商品图片，视频等等)关联id查询资源的id与MD5值
     * @param queryParam  包含属性recordId  资源关联id（必传值）;
     *                    dataType  查询该类型资源时为必传值----（1营业执照id，2，商品详情图片id，3商品轮播id,4：店铺LOGO，5：店铺图片，6商品品论图片-）;
     *                    type 查询视频广告类资源时为必传值------（类型： 0 视频图片，1 视频，2是广告视频，3是广告图片）
     * @return
     */
    List<Map> queryResourceByRecordId (QueryParam queryParam , HttpServletRequest req);
    //遍历查询的数据更具图片ID转换为resourceid
    List<Map> addImages(List<Map> list, Integer dataType, HttpServletRequest req);
    List<Map> addVideos(List<Map> list, HttpServletRequest req);

    Long addImagesreturnRefId(MultipartFile[] files, Integer dateType);

    Long addImageRefId(Long[] files, Integer dateType);

    List<FileRef> queryByRefId(QueryParam queryParam);

    List<Map> queryTypeByRefId(QueryParam queryParam,HttpServletRequest req);


}
