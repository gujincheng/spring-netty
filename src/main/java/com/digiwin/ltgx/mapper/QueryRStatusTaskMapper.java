package com.digiwin.ltgx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digiwin.ltgx.domain.QueryRStatusTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 获取当前R状态任务，通过carrierNo
 */
@Mapper
public interface QueryRStatusTaskMapper extends BaseMapper<QueryRStatusTask> {

    /**
     * 获取当前R状态任务，通过carrierNo
     *
     * @param machineNo machineNo
     * @param plotNo plotNo
     * @return QueryRStatusTask
     */
    List<QueryRStatusTask> queryRStatusTask(@Param("machineNo") String machineNo,@Param("plotNo") String plotNo);

}
