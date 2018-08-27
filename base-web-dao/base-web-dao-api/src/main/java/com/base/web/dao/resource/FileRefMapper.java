package com.base.web.dao.resource;

import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.resource.FileRef;
import com.base.web.dao.GenericMapper;


import java.util.List;
import java.util.Map;

/**
 * 资源中间表dao接口
 */
public interface FileRefMapper extends GenericMapper<FileRef,Long> {
    void deleteResourceByRefId(Long id);
    Long[] queryFileRefByRefId(FileRef fileRef);
    int shopUpdateFileRef(FileRef fileRef);

    /**
     * 根据资源(商品轮播，商品图片等等)关联id查询资源的id与MD5值
     * @param queryParam  包含参数recordId  资源关联id（必传值），
     *                    dataType  查询该类型资源时为必传值-----------（1营业执照id，2，商品详情图片id，3商品轮播id,4：店铺LOGO，5：店铺图片，6商品品论图片-）
     *                    type 查询视频广告类资源时为必传值--------------（类型： 0 视频图片，1 视频，2是广告视频，3是广告图片）
     * @return
     */
   List<Map> queryResourceByRecordId (QueryParam queryParam);

   List<FileRef> queryByRefId(QueryParam queryParam);

   List<Map> queryTypeByRefId(QueryParam queryParam);
   int deleteByRefIdResourceIdDateType(QueryParam queryParam);
}
