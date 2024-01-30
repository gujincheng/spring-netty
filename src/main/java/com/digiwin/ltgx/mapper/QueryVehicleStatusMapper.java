package com.digiwin.ltgx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digiwin.ltgx.domain.QueryVehicleStatusDetail;
import com.digiwin.ltgx.domain.VehicleLotnoDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 查询载具状态，通过carrierNo
 */
@Mapper
public interface QueryVehicleStatusMapper extends BaseMapper<QueryVehicleStatusDetail> {

    /**
     * 查询载具状态，通过carrierNo
     *
     * @param carrierNo carrierNo
     * @return QueryVehicleStatusDetail
     */
    List<QueryVehicleStatusDetail> queryVehicleStatus(@Param("carrierNo") String carrierNo);

}
