package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.math.BigDecimal;
import java.util.Date;

public class LikeRecord extends GenericPo<Long> {
    //视频秀id（用户视频表video_id主键）
    private Long videoGoodsId;
    //点赞用户id
    private Long userId;


    public Long getVideoGoodsId() {
        return videoGoodsId;
    }

    public void setVideoGoodsId(Long videoGoodsId) {
        this.videoGoodsId = videoGoodsId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public interface Property extends GenericPo.Property {
        //品牌id
        String videoGoodsId="videoGoodsId";
        String userId="userId";
//        String status="status";
    }
}
