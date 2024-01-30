package com.digiwin.ltgx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digiwin.ltgx.domain.ProductionTaskInMESDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 判断生产批是否存在于MES，通过plotNo
 */
@Mapper
public interface ProductionTaskInMESMapper extends BaseMapper<ProductionTaskInMESDetail> {

    /**
     * 判断生产批是否存在于MES，通过plotNo
     *
     * @param plotNo plotNo
     * @return ProductionTaskInMESDetailList
     */
    List<ProductionTaskInMESDetail> productionTaskInMES(@Param("plotNo") String plotNo);

}
