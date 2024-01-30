package com.digiwin.ltgx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digiwin.ltgx.domain.QueryWhetherAuxiliaryExpiredDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 获取辅料是否过期,在 进站-14 中用到 【【进站14暂时不用做】】
 */
@Mapper
public interface QueryWhetherAuxiliaryExpiredMapper extends BaseMapper<QueryWhetherAuxiliaryExpiredDetail> {

    /**
     * 获取辅料是否过期,在 进站-14 中用到 【【进站14暂时不用做】】
     * @param machineNo machineNo
     * @return QueryWhetherAuxiliaryExpiredDetail
     */
    List<QueryWhetherAuxiliaryExpiredDetail> queryWhetherAuxiliaryExpired(@Param("machineNo") String machineNo);

}
