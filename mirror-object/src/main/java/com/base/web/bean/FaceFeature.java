package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.util.Date;

public class FaceFeature extends GenericPo<Long> {
    //视频id
    private  Long videoId;
    //人脸特征值
    private byte[] faceFeature;

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public byte[] getFaceFeature() {
        return faceFeature;
    }

    public void setFaceFeature(byte[] faceFeature) {
        this.faceFeature = faceFeature;
    }

    public interface Property extends GenericPo.Property {
        String videoId="videoId";
        String faceFeature="faceFeature";
    }
}
