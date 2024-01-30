package com.digiwin.ltgx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digiwin.ltgx.domain.QueryVehicleCleanDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 载具清空查询
 */
@Mapper
public interface QueryVehicleCleanMapper extends BaseMapper<QueryVehicleCleanDetail> {

    /**
     * 载具清空查询
     * @param carrierNo carrierNo
     * @return QueryVehicleCleanDetail
     */
    List<QueryVehicleCleanDetail> queryVehicleClean(@Param("carrierNo") String carrierNo);

}
