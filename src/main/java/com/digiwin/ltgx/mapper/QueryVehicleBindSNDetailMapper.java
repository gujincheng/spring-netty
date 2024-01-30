package com.digiwin.ltgx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digiwin.ltgx.domain.QueryVehicleBindSNDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 获取是否验证sn长度
 */
@Mapper
public interface QueryVehicleBindSNDetailMapper extends BaseMapper<QueryVehicleBindSNDetail> {

    /**
     * 查询载具绑定的sn信息
     * @param carrierNo 载具编号
     * @return QueryVehicleBindSNDetail
     */
    List<QueryVehicleBindSNDetail> queryVehicleBindSNDetail(@Param("carrierNo") String carrierNo);

}
