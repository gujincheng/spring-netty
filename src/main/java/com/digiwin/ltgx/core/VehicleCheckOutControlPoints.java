package com.digiwin.ltgx.core;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.digiwin.ltgx.domain.*;
import com.digiwin.ltgx.dto.ResponseDTO;
import com.digiwin.ltgx.enums.ResponseCode;
import com.digiwin.ltgx.mapper.*;
import com.digiwin.ltgx.utils.DateUtils;
import com.digiwin.ltgx.utils.LogUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.digiwin.ltgx.utils.DateUtils.YYYYMMDDHHMMSSSSS;

/**
 * 出站卡控逻辑
 */
@Log4j2
@Component("vehicleCheckOutControlPoints")
public class VehicleCheckOutControlPoints {
    @Autowired
    private QueryVehicleStatusMapper queryVehicleStatusMapper; // 出站-管控点1
    @Autowired
    private QueryOpnoTimeSettingMapper queryOpnoTimeSettingMapper; // 出站-管控点5

    @Autowired
    private GetVehicleLotnoByCarrierNoMapper getVehicleLotnoByCarrierNoMapper; //出站-管控点3，出站-管控点6

    @Autowired
    private ProductionTaskInMESMapper productionTaskInMESMapper;  // 出站-管控点3

    @Autowired
    private QueryVehicleBindProductionSNInfoMapper queryVehicleBindProductionSNInfoMapper; // 出站-管控点4

    public JSONObject checkOutControlPointLogic(String machineNo,String carrierNo,String opNo){
        String baseLog = String.format("machineNo: %s,carrierNo:%s,opNo:%s ############## ",machineNo,carrierNo,opNo);
        //查询载具状态
        StopWatch stopWatch = StopWatch.createStarted();
        log.debug(baseLog + "出站开始时间：" + DateUtils.stampToDateTime(stopWatch.getStartTime(),YYYYMMDDHHMMSSSSS));
        List<QueryVehicleStatusDetail> queryVehicleStatusDetailList = queryVehicleStatusMapper.queryVehicleStatus(carrierNo);
        log.debug(baseLog + "出站-管控点1:queryVehicleStatusDetailList执行时间：" + LogUtil.splitTime(stopWatch));
        // 已和建男确认，queryVehicleStatusDetailList为空，返回成功
        if (ObjectUtils.isNotEmpty(queryVehicleStatusDetailList)){
            // //出站-管控点1
            String prodStatus = queryVehicleStatusDetailList.get(0).getPRODSTATUS();
            if(null != prodStatus && !prodStatus.equals("R")){
                log.debug(baseLog + "出站-管控点1: 载具状态不是R状态");
                return ResponseDTO.build(ResponseCode.CHECKOUT_ERROR_CONTROL_POINT_1);
            }
            log.debug(baseLog + "出站-管控点1执行时间：" + LogUtil.splitTime(stopWatch));
            //
            //获取作业站设置的Q-TIME、PLLTime、CycleTime时间
            List<QueryOpnoTimeSettingDetail> queryOpnoTimeSettingDetails = queryOpnoTimeSettingMapper.queryOpnoTimeSetting(opNo);
            log.debug(baseLog + "出站-管控点5:queryOpnoTimeSettingMapper执行时间：" + LogUtil.splitTime(stopWatch));
            //出站-管控点5
            String isCycleTime = queryOpnoTimeSettingDetails.get(0).getISCYCLETIME();
            String timeInfo = queryVehicleStatusDetailList.get(0).getTIMEINFO();
            double cycleTime = queryOpnoTimeSettingDetails.get(0).getCYCLETIME();
            if("1".equals(isCycleTime)){
                long diffMin = DateUtils.diffMin(DateUtils.getNowDate(),DateUtils.parseDate(timeInfo));
                if(diffMin > cycleTime){
                    log.debug(baseLog + "出站-管控点5: Cycle time检查项超出标准时间");
                    return ResponseDTO.build(ResponseCode.CHECKOUT_ERROR_CONTROL_POINT_5);
                }
            }
            log.debug(baseLog + "出站-管控点5执行时间：" + LogUtil.splitTime(stopWatch));
            //出站-管控点4
            String isPot = queryOpnoTimeSettingDetails.get(0).getISPOT();
            if("1".equals(isPot)){
                //获取载具绑定生产批产品SN信息
                List<QueryVehicleBindProductionSNInfoDetail> queryVehicleBindProductionSNInfoDetails = queryVehicleBindProductionSNInfoMapper.queryVehicleBindProductionSNInfo(carrierNo);
                log.debug(baseLog + "出站-管控点4:queryVehicleBindProductionSNInfoMapper执行时间：" + LogUtil.splitTime(stopWatch));
                if (ObjectUtils.isNotEmpty(queryVehicleBindProductionSNInfoDetails)){
                    log.debug(baseLog + "出站-管控点4: HR镀膜BD面锅次号管控");
                    if (ObjectUtils.isEmpty(queryVehicleBindProductionSNInfoDetails.get(0).getPOTNO())){
                        return ResponseDTO.build(ResponseCode.CHECKOUT_ERROR_CONTROL_POINT_4);
                    }
                } else {
                    log.debug(baseLog + "出站-管控点4: HR镀膜BD面锅次号管控");
                    return ResponseDTO.build(ResponseCode.CHECKOUT_ERROR_CONTROL_POINT_4);
                }
            }
            log.debug(baseLog + "出站-管控点4: 执行时间：" + LogUtil.splitTime(stopWatch));
        }

        // 获取载具相关信息
        List<VehicleLotnoDetail> vehicleLotnoDetailList = getVehicleLotnoByCarrierNoMapper.getVehicleLotnoByCarrierNo(carrierNo,opNo,null,null);
        log.debug(baseLog + "出站-管控点6:getVehicleLotnoByCarrierNoMapper执行时间：" + LogUtil.splitTime(stopWatch));
        // 这里已和建男确认出站6可以使用出站3的sql，这样sql就统一了，减少执行一次SQL
        // 已和建男确认，vehicleLotnoDetailList为空，报错：提示信息同进站6
        if (ObjectUtils.isNotEmpty(vehicleLotnoDetailList)){
            // 出站-管控点6
            String riskStatus = vehicleLotnoDetailList.get(0).getRISKSTATUS();
            if ("1".equals(riskStatus)){
                log.debug(baseLog + "出站-管控点6:  被风险标记过的");
                return ResponseDTO.build(ResponseCode.CHECKOUT_ERROR_CONTROL_POINT_6);
            }
            log.debug(baseLog + "出站-管控点6: 执行时间：" + LogUtil.splitTime(stopWatch));
            // 出站-管控点3
            String plotNo = vehicleLotnoDetailList.get(0).getLOTNO().replaceAll("\uFEFF", "");
            if(ObjectUtils.isNotEmpty(plotNo)){
                // 判断生产批是否存在于MES
                List<ProductionTaskInMESDetail> productionTaskInMESDetailList = productionTaskInMESMapper.productionTaskInMES(plotNo);
                log.debug(baseLog + "出站-管控点3:-productionTaskInMESMapper执行时间：" + LogUtil.splitTime(stopWatch));
                if (productionTaskInMESDetailList.size() == 0){
                    log.debug(baseLog + "出站-管控点3: 未找到生产批信息 请确认生产批号 {} 是否为MES中存在的生产批号",plotNo);
                    return ResponseDTO.build(ResponseCode.CHECKOUT_ERROR_CONTROL_POINT_3);
                }
            } else {
                log.debug(baseLog + "出站-管控点3: 未找到生产批信息 plotNo为空");
                return ResponseDTO.build(ResponseCode.UNKNOWN_ERROR,"出站-管控点3:获取plotNo为空，无法找到生产批信息");
            }
            log.debug(baseLog + "出站-管控点3: 执行时间：" + LogUtil.splitTime(stopWatch));
        } else {
            log.debug(baseLog + "出站-管控点3: 系统中找不到载具绑定过的SN信息，请先绑定SN数据");
            return ResponseDTO.build(ResponseCode.CHECKIN_ERROR_CONTROL_POINT_6);
        }

        return ResponseDTO.build(ResponseCode.CHECKOUT_SUCCESS);
    }

}
