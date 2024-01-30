package com.digiwin.ltgx.core;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.digiwin.ltgx.domain.QueryVehicleBindSNDetail;
import com.digiwin.ltgx.dto.ResponseDTO;
import com.digiwin.ltgx.enums.ResponseCode;
import com.digiwin.ltgx.mapper.QueryVehicleBindSNDetailMapper;
import com.digiwin.ltgx.utils.DateUtils;
import com.digiwin.ltgx.utils.LogUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.digiwin.ltgx.utils.DateUtils.YYYYMMDDHHMMSSSSS;

/**
 * 查询卡控逻辑
 */
@Component("vehicleQueryControlPoints")
@Log4j2
public class VehicleQueryControlPoints {
    @Autowired
    private QueryVehicleBindSNDetailMapper queryVehicleBindSNDetailMapper; // 查询1

    public JSONObject queryCardControlLogic(String machineNo,String carrierNo){
        String baseLog = String.format("machineNo: %s,carrierNo:%s ############## ",machineNo,carrierNo);
        StopWatch stopWatch = StopWatch.createStarted();
        log.debug(baseLog + "查询开始时间：" + DateUtils.stampToDateTime(stopWatch.getStartTime(),YYYYMMDDHHMMSSSSS));
        List<QueryVehicleBindSNDetail> queryVehicleBindSNDetails = queryVehicleBindSNDetailMapper.queryVehicleBindSNDetail(carrierNo);
        log.debug(baseLog + "getVehicleLotnoByCarrierNoMapper执行时间：" + LogUtil.splitTime(stopWatch));
        // 查询1
        if (ObjectUtils.isEmpty(queryVehicleBindSNDetails)){
            return ResponseDTO.build(ResponseCode.CHECKQUERY_ERROR_CONTROL_POINT_1);
        } else {
            log.debug( baseLog + ResponseDTO.build(ResponseCode.CHECKQUERY_SUCCESS,queryVehicleBindSNDetails));
            return ResponseDTO.build(ResponseCode.CHECKQUERY_SUCCESS,queryVehicleBindSNDetails);
        }
    }
}
