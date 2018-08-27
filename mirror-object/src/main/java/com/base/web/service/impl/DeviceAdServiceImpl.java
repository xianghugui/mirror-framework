package com.base.web.service.impl;

import com.base.web.bean.DeviceAd;
import com.base.web.bean.common.InsertParam;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.bean.po.resource.FileRef;
import com.base.web.dao.DeviceAdMapper;
import com.base.web.dao.resource.FileRefMapper;
import com.base.web.dao.resource.ResourcesMapper;
import com.base.web.service.DeviceAdService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 10:03  2018/3/28
 */
@Service("DeviceAdService")
public class DeviceAdServiceImpl extends AbstractServiceImpl<DeviceAd, Long> implements DeviceAdService{

    @Resource
    private DeviceAdMapper deviceAdMapper;

    @Resource
    private ResourcesMapper resourcesMapper;

    @Resource
    private FileRefMapper fileRefMapper;
    
    @Override
    protected DeviceAdMapper getMapper() {
        return this.deviceAdMapper;
    }

    /**
     * 插入广告信息，同时往关联表 插入关联关系
     * @param data 要添加的数据
     * @return
     */
    @Override
    @Transactional
    public Long insert(DeviceAd data) {
        if (data.getId() == null) {
            data.setId(GenericPo.createUID());
        }
        data.setStatus(0);
        data.setCreateTime(new Date());
        data.setAdDataId(GenericPo.createUID());
        Long[] list=data.getAdList();
        getInsertMapper().insert(InsertParam.build(data));
        // 插入 t_file_ref 中间表
        for(int i=0;i<list.length;i++){
            //判断是视频还是图片广告
            if(resourcesMapper.selectByPk(list[i]).getName().contains(".mp4")){
                //资源关联ID，主键，数据类型：（2：广告视频，3：广告图片），广告优先级
                this.insertAd(list[i],data.getAdDataId(),2,i+1);
            }
            else {
                this.insertAd(list[i], data.getAdDataId(), 3, i + 1);
            }
        }
        return data.getId();
    }

    //插入广告
    @Transactional
    public int insertAd(Long videoId,Long recordId,int type,int priority){
        FileRef fileRef = new FileRef();
        fileRef.setId(GenericPo.createUID());
        fileRef.setRefId(recordId);
        fileRef.setResourceId(videoId);
        fileRef.setType(type);
        fileRef.setPriority(priority);
        int flag = fileRefMapper.insert(InsertParam.build(fileRef));
        return flag;
    }

    //修改广告
    public int update(DeviceAd data){
        tryValidPo(data);
        createUpdate().fromBean(data).where(GenericPo.Property.id).exec();
        DeviceAd deviceAd=getMapper().selectByPk(data.getId());
        Long adDataId=deviceAd.getAdDataId();
        Long[] list=data.getAdList();
        deleteFileRefById(adDataId);
        for(int i=0;i<list.length;i++){
            //判断是视频还是图片广告
            if(resourcesMapper.selectByPk(list[i]).getName().contains(".mp4")){
                this.insertAd(list[i],adDataId,2,i+1);
            }
            else {
                this.insertAd(list[i], adDataId, 3, i + 1);
            }
        }
        return 1;

    }

    //根据id删除关联数据
    private void deleteFileRefById(Long oldRecordId){
        FileRef fileRef=new FileRef();
        fileRef.setRefId(oldRecordId);
        Long[] list = fileRefMapper.queryFileRefByRefId(fileRef);
        for(int i=0;i<list.length;i++){
            fileRefMapper.deleteResourceByRefId(list[i]);
        }
    }

    // >>查询全部设备广告
    @Override
    public PagerResult<Map> queryAllAd(QueryParam param){
        PagerResult<Map> pagerResult = new PagerResult<>();
        param.setPaging(false);
        int total = getMapper().total(param);

        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            //根据实际记录数量重新指定分页参数
            param.rePaging(total);
            pagerResult.setData(getMapper().queryAllAd(param));
        }
        return pagerResult;
    }

    // >>根据广告id查询设备广告
    @Override
    public Map queryAdById(Long adId){
        Map list = getMapper().queryAdById(adId);
        QueryParam queryParam = new QueryParam();
        queryParam.getParam().put("refId",list.get("adDataId"));
        List<Map> resourceUrlList = fileRefMapper.queryTypeByRefId(queryParam);
        ArrayList<String> resources = new ArrayList<String>();
        ArrayList<String> resourcesType = new ArrayList<String>();
        if(resourceUrlList.size() > 0) {
            for (int i = 0; i < resourceUrlList.size(); i++) {
                // 资源图片判断条件
                if (resourceUrlList.get(i).get("type").toString().equals("3")) {
                    // 资源图片
                    resources.add("/file/image/"+resourceUrlList.get(i).get("resourceId"));
                }
                // 资源视频判断条件
                if (resourceUrlList.get(i).get("type").toString().equals("2")) {
                    // 资源视频
                    resources.add("/file/download/"+resourceUrlList.get(i).get("resourceId"));
                }
                resourcesType.add(resourceUrlList.get(i).get("type").toString());
            }
            list.put("resources",resources);
            list.put("resourcesType",resourcesType);
        }
        return list;
    }

    @Override
    public int updateStatusByUId(Long uId,Integer status){
        return deviceAdMapper.updateStatusByUId(uId,status);
    }


    /**
     * 查询设备的设备广告信息
     * @param deviceName 设备名
     * @return
     */
    @Override
    public Map queryAdByDevice(String deviceName){
        return getMapper().queryAdByDevice(deviceName);
    }
}
