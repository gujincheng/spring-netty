package com.digiwin.ltgx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digiwin.ltgx.domain.QueryProductionVehicleStatusDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 获取当前生产批载具信息
 */
@Mapper
public interface QueryProductionVehicleStatusMapper extends BaseMapper<QueryProductionVehicleStatusDetail> {

    /**
     * 获取当前生产批载具信息
     * @param plotNo plotNo
     * @param carrierNo carrierNo
     * @return alert list
     */
    List<QueryProductionVehicleStatusDetail> queryProductionVehicleStatus(@Param("plotNo") String plotNo,
                                                                          @Param("carrierNo") String carrierNo);

}
