package com.base.web.dao;

import com.base.web.bean.Logistics;
import org.apache.ibatis.annotations.Param;


public interface LogisticsMapper extends GenericMapper<Logistics,Integer> {
    /**
     * 根据快递名称----是否插入当前快递
     * @param logistics
     * @return
     */
    Integer insertLogistics(Logistics logistics);

    /**
     * 查询当前快递公司是否存在
     * @param name
     * @return
     */
    Integer queryLogisticsByName(@Param("name") String name);

}
