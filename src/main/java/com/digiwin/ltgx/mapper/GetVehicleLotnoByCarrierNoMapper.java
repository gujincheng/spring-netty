package com.digiwin.ltgx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digiwin.ltgx.domain.VehicleLotnoDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 获取载具相关信息，通过carrierNo
 */
@Mapper
public interface GetVehicleLotnoByCarrierNoMapper extends BaseMapper<VehicleLotnoDetail> {

    /**
     * 获取载具相关信息，通过carrierNo
     *
     * @param carrierNo carrierNo
     * @param opNo opNo
     * @param prodStatus prodStatus
     * @param issueState issueState
     * @return VehicleLotnoDetailList
     */
    List<VehicleLotnoDetail> getVehicleLotnoByCarrierNo(@Param("carrierNo") String carrierNo,
                                                        @Param("opNo") String opNo,
                                                        @Param("prodStatus") String prodStatus,
                                                        @Param("issueState") String issueState);

}
