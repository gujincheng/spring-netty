package com.digiwin.ltgx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digiwin.ltgx.domain.QuerySNnoByLotnoOpnoVehicleNoDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 获取是否验证sn长度
 */
@Mapper
public interface QuerySNnoByLotnoOpnoVehicleNoMapper extends BaseMapper<QuerySNnoByLotnoOpnoVehicleNoDetail> {

    /**
     * 获取是否验证sn长度
     * @param carrierNo 载具编号
     * @param plotNo 生产批编号
     * @param opNo 作业站编号
     * @return QuerySNnoByLotnoOpnoVehicleNoDetail
     */
    List<QuerySNnoByLotnoOpnoVehicleNoDetail> querySNnoByLotnoOpnoVehicleNo(@Param("carrierNo") String carrierNo,
                                                                            @Param("plotNo") String plotNo,
                                                                            @Param("opNo") String opNo);

}
