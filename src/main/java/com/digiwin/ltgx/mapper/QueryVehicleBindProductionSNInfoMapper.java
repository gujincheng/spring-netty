package com.digiwin.ltgx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digiwin.ltgx.domain.QueryVehicleBindProductionSNInfoDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 获取载具绑定生产批产品SN信息
 */
@Mapper
public interface QueryVehicleBindProductionSNInfoMapper extends BaseMapper<QueryVehicleBindProductionSNInfoDetail> {

    /**
     * 获取载具绑定生产批产品SN信息
     * @param carrierNo carrierNo
     * @return QueryVehicleBindProductionSNInfoDetail
     */
    List<QueryVehicleBindProductionSNInfoDetail> queryVehicleBindProductionSNInfo(@Param("carrierNo") String carrierNo);

}
