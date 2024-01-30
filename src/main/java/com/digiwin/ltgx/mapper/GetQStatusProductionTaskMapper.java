package com.digiwin.ltgx.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digiwin.ltgx.domain.QStatusProductionTaskDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 获取当前Q状态生产任务
 */
@Mapper
public interface GetQStatusProductionTaskMapper extends BaseMapper<QStatusProductionTaskDetail> {

    /**
     * 获取当前Q状态生产任务
     *
     * @param machineNo machineNo
     * @param plotNo plotNo
     * @return QStatusProductionTaskDetailList
     */
    List<QStatusProductionTaskDetail> getQStatusProductionTask(@Param("machineNo") String machineNo,
                                                               @Param("plotNo") String plotNo);

}
