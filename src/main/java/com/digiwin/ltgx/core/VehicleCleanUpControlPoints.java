package com.digiwin.ltgx.core;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.digiwin.ltgx.domain.VehicleLotnoDetail;
import com.digiwin.ltgx.dto.ResponseDTO;
import com.digiwin.ltgx.enums.ResponseCode;
import com.digiwin.ltgx.mapper.GetVehicleLotnoByCarrierNoMapper;
import com.digiwin.ltgx.utils.DateUtils;
import com.digiwin.ltgx.utils.LogUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.digiwin.ltgx.utils.DateUtils.YYYYMMDDHHMMSSSSS;

/**
 * 载具清空卡控逻辑
 */
@Component("vehicleCleanUpControlPoints")
@Log4j2
public class VehicleCleanUpControlPoints {
    @Autowired
    private GetVehicleLotnoByCarrierNoMapper getVehicleLotnoByCarrierNoMapper; // 管控点2、管控点6、管控点7

    public JSONObject cleanUpControlPointLogic(String machineNo,String carrierNo,String opNo){
        String baseLog = String.format("machineNo: %s,carrierNo:%s,opNo:%s ############## ",machineNo,carrierNo,opNo);
        StopWatch stopWatch = StopWatch.createStarted();
        log.debug(baseLog + "载具清空开始时间：" + DateUtils.stampToDateTime(stopWatch.getStartTime(),YYYYMMDDHHMMSSSSS));
        List<VehicleLotnoDetail> vehicleLotnoDetailList = getVehicleLotnoByCarrierNoMapper.getVehicleLotnoByCarrierNo(carrierNo,opNo,"Q","2");
        log.debug("getVehicleLotnoByCarrierNoMapper执行时间：" + LogUtil.splitTime(stopWatch));
        if (ObjectUtils.isNotEmpty(vehicleLotnoDetailList)){
            // 载具清空-管控点2
            String riskStatus = vehicleLotnoDetailList.get(0).getRISKSTATUS();
            if ("1".equals(riskStatus)){
                log.debug(baseLog + " 载具清空管控点2: 被风险标记过的");
                return ResponseDTO.build(ResponseCode.CHECKCLEANUP_ERROR_CONTROL_POINT_2);
            }
        } else {
            // 进站：载具清空-管控点2
            log.debug(baseLog + "载具清空管控点2: 若【载具清空数据查询】查无资料则无需清空");
            return ResponseDTO.build(ResponseCode.CHECKCLEANUP_ERROR_CONTROL_POINT_2);
        }
        return ResponseDTO.build(ResponseCode.CHECKOUT_SUCCESS);
    }
}
