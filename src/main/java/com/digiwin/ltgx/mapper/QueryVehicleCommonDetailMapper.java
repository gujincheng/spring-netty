package com.digiwin.ltgx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digiwin.ltgx.domain.QueryVehicleCommonDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 查询载具基本信息,在 绑定-1 中用到
 */
@Mapper
public interface QueryVehicleCommonDetailMapper extends BaseMapper<QueryVehicleCommonDetail> {

    /**
     * 查询载具基本信息,在 绑定-1 中用到
     * @param carrierNo carrierNo
     * @return QueryVehicleCommonDetail
     */
    List<QueryVehicleCommonDetail> queryVehicleCommonDetail(@Param("carrierNo") String carrierNo);

}
