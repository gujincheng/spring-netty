package com.digiwin.ltgx.core;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.digiwin.ltgx.domain.QueryVehicleCommonDetail;
import com.digiwin.ltgx.domain.QueryVehicleStatusDetail;
import com.digiwin.ltgx.domain.VehicleLotnoDetail;
import com.digiwin.ltgx.dto.ResponseDTO;
import com.digiwin.ltgx.enums.ResponseCode;
import com.digiwin.ltgx.mapper.*;
import com.digiwin.ltgx.utils.DateUtils;
import com.digiwin.ltgx.utils.LogUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.digiwin.ltgx.utils.DateUtils.YYYYMMDDHHMMSSSSS;

/**
 * 绑定卡控逻辑
 * 顺序：3、1、5、6、7
 */
@Component("vehicleBindingControlPoints")
@Log4j2
public class VehicleBindingControlPoints {
    @Autowired
    private QueryVehicleStatusMapper queryVehicleStatusMapper; // 绑定-管控点3

    @Autowired
    private GetVehicleLotnoByCarrierNoMapper getVehicleLotnoByCarrierNoMapper; // 绑定-管控点5

    @Autowired
    private QueryVehicleCommonDetailMapper queryVehicleCommonDetailMapper;   //绑定-管控点1

    public JSONObject bindingCardControlLogic(String machineNo,String carrierNo,String opNo,String plotNo,List<String> snList){
        String baseLog = String.format("machineNo: %s,carrierNo:%s,opNo:%s,plotNo:%s ############## ",machineNo,carrierNo,opNo,plotNo);
        StopWatch stopWatch = StopWatch.createStarted();
        if(ObjectUtils.isNotEmpty(snList)){
            //查询载具状态
            log.debug(baseLog + "绑定开始时间：" + DateUtils.stampToDateTime(stopWatch.getStartTime(),YYYYMMDDHHMMSSSSS));
            List<QueryVehicleStatusDetail> queryVehicleStatusDetailList = queryVehicleStatusMapper.queryVehicleStatus(carrierNo);
            log.debug(baseLog + "queryVehicleStatusDetailList执行时间：" + LogUtil.splitTime(stopWatch));
            if (ObjectUtils.isNotEmpty(queryVehicleStatusDetailList)){
                // 绑定-管控点3
                String prodStatus = queryVehicleStatusDetailList.get(0).getPRODSTATUS();
                if(null != prodStatus && prodStatus.equals("R")){
                    log.debug(baseLog + "绑定-管控点3: 该载具状态为R（生产状态）不允许进行绑定");
                    return ResponseDTO.build(ResponseCode.CHECKBINDING_ERROR_CONTROL_POINT_3);
                }
                log.debug(baseLog + "绑定-管控点3执行时间：" + LogUtil.splitTime(stopWatch));
            }
        } else {
            return ResponseDTO.build(ResponseCode.CHECKBINDING_ERROR_CONTROL_POINT_3);
        }
        //绑定-管控点1
        List<QueryVehicleCommonDetail> queryVehicleCommonDetails = queryVehicleCommonDetailMapper.queryVehicleCommonDetail(carrierNo);
        if(ObjectUtils.isNotEmpty(queryVehicleCommonDetails)){
            if("1".equals(queryVehicleCommonDetails.get(0).getRISKSTATUS())){
                log.debug(baseLog + "绑定-管控点1: 该载具状态已标记为风险，无法进出站");
                return ResponseDTO.build(ResponseCode.CHECKBINDING_ERROR_CONTROL_POINT_9);
            }
            if (snList.size() > queryVehicleCommonDetails.get(0).getVEHICLECAPACITY()){
                log.debug(baseLog + "绑定-管控点1: sn数量上传大于载具上限数量，或者现场使用载具未录入MES系统");
                return ResponseDTO.build(ResponseCode.CHECKBINDING_ERROR_CONTROL_POINT_1);
            }
        } else {
            log.debug(baseLog + "绑定-管控点1: 系统中找不到载具信息");
            return ResponseDTO.build(ResponseCode.CHECKBINDING_ERROR_CONTROL_POINT_10);
        }

        //绑定-管控点5,6
        List<String> snListFilter = snList.stream().filter(x -> !x.contains("-")).collect(Collectors.toList());
        boolean controlPoint5Flag = false;
        boolean controlPoint6Flag = false;
        Set controlPoint7Set = new HashSet();
        for (int i = 0; i < snListFilter.size(); i++) {
            String snNumber = snListFilter.get(i);
            if(plotNo.startsWith("V3") && !(snNumber.startsWith("00") || snNumber.startsWith("7c"))){
                controlPoint6Flag = true;
            }
            if(plotNo.startsWith("V4") && !(snNumber.startsWith("10") || snNumber.startsWith("88"))){
                controlPoint6Flag = true;
            }

            if((plotNo.startsWith("10") && snNumber.startsWith("00")) || (plotNo.startsWith("88") && snNumber.startsWith("7c"))){
                controlPoint5Flag = true;
            }
            controlPoint7Set.add(snNumber.substring(0,2));
        }
        //绑定-管控点5
        if(controlPoint5Flag){
            log.debug(baseLog + "绑定-管控点5: 长条sn00开头不能和10开头的绑定，棱镜sn7c开头不能和88开头绑定");
            return ResponseDTO.build(ResponseCode.CHECKBINDING_ERROR_CONTROL_POINT_5);
        }
        //绑定-管控点6
        if(controlPoint6Flag){
            log.debug(baseLog + "绑定-管控点6: 长条sn00和棱镜sn7c开头只能绑定V3开头批次，长条sn10和棱镜sn88开头的只能绑定V4开头的");
            return ResponseDTO.build(ResponseCode.CHECKBINDING_ERROR_CONTROL_POINT_6);
        }

        //绑定-管控点7
        if (controlPoint7Set.size() > 1){
            log.debug(baseLog + "绑定-管控点7: 接口绑定，绑定sn为空时也报绑定版本不一致。");
            return ResponseDTO.build(ResponseCode.CHECKBINDING_ERROR_CONTROL_POINT_7);
        }


        return ResponseDTO.build(ResponseCode.CHECKBINDING_SUCCESS);
    }
}
