package com.digiwin.ltgx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digiwin.ltgx.domain.QueryOpnoTimeSettingDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 获取是否验证sn长度
 */
@Mapper
public interface QueryOpnoTimeSettingMapper extends BaseMapper<QueryOpnoTimeSettingDetail> {

    /**
     * 获取是否验证sn长度
     * @param opNo 作业站编号
     * @return QuerySNnoByLotnoOpnoVehicleNoDetail
     */
    List<QueryOpnoTimeSettingDetail> queryOpnoTimeSetting(@Param("opNo") String opNo);

}
