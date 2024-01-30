package com.digiwin.ltgx.core;

import cn.hutool.core.util.StrUtil;
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
import java.util.stream.Collectors;

import static com.digiwin.ltgx.utils.DateUtils.YYYYMMDDHHMMSSSSS;

/**
 * 进站卡控逻辑
 * 进站-15，进站-16暂时不需要做
 */

@Log4j2
@Component("vehicleCheckInControlPoints")
public class VehicleCheckInControlPoints {

    @Autowired
    private GetVehicleLotnoByCarrierNoMapper getVehicleLotnoByCarrierNoMapper; // 管控点2、管控点6、管控点7

    @Autowired
    private ProductionTaskInMESMapper productionTaskInMESMapper;  // 管控点2

    @Autowired
    private QueryVehicleStatusMapper queryVehicleStatusMapper; // 管控点4

    @Autowired
    private QuerySNnoByLotnoOpnoVehicleNoMapper querySNnoByLotnoOpnoVehicleNoMapper; // 管控点7

    @Autowired
    private QueryOpnoTimeSettingMapper queryOpnoTimeSettingMapper; // 管控点8

    @Autowired
    private QueryVehicleBindProductionSNInfoMapper queryVehicleBindProductionSNInfoMapper; // 管控点13

    @Autowired
    private QueryWhetherTestSNLengthMapper queryWhetherTestSNLengthMapper; // 管控点13


    public JSONObject checkInControlPointLogic(String transactionId,String machineNo, String carrierNo, String opNo){
        String baseLog = String.format("transactionId: %s, machineNo: %s,carrierNo:%s,opNo:%s ################# ",transactionId,machineNo,carrierNo,opNo);
        // 获取载具相关信息
        StopWatch stopWatch = StopWatch.createStarted();
        log.debug(baseLog + "进站开始时间：" + DateUtils.stampToDateTime(stopWatch.getStartTime(),YYYYMMDDHHMMSSSSS));
        List<VehicleLotnoDetail> vehicleLotnoDetailList = getVehicleLotnoByCarrierNoMapper.getVehicleLotnoByCarrierNo(carrierNo,opNo, null,null);
        log.debug(baseLog + "进站-getVehicleLotnoByCarrierNoMapper执行时间：" + LogUtil.splitTime(stopWatch));
        // 管控点6，管控点9、管控点7、管控点2
        if (ObjectUtils.isNotEmpty(vehicleLotnoDetailList)){
            // 管控点9
            String riskStatus = vehicleLotnoDetailList.get(0).getRISKSTATUS();
            if ("1".equals(riskStatus)){
                log.debug(baseLog + "进站-管控点9: 被风险标记过的");
                return ResponseDTO.build(ResponseCode.CHECKIN_ERROR_CONTROL_POINT_9);
            }
            log.debug(baseLog + "进站-管控点9执行时间：" + LogUtil.splitTime(stopWatch));

            // 管控点4
            String prodStatus = vehicleLotnoDetailList.get(0).getPRODSTATUS();
            if(ObjectUtils.isNotEmpty(prodStatus) && !prodStatus.equals("Q")){
                log.debug(baseLog + "进站-管控点4: 载具状态不是Q状态");
                return ResponseDTO.build(ResponseCode.CHECKIN_ERROR_CONTROL_POINT_4);
            }
            // 管控点2
            if (ObjectUtils.isEmpty(vehicleLotnoDetailList.get(0).getBASELOTNO())){
                log.debug(baseLog + "进站-管控点2：未找到生产批信息 请确认生产批号 {} 是否为MES中存在的生产批号",vehicleLotnoDetailList.get(0).getLOTNO());
                return ResponseDTO.build(ResponseCode.CHECKIN_ERROR_CONTROL_POINT_2);
            }
            // 管控点7
            String queryOpNo = null == opNo ? vehicleLotnoDetailList.get(0).getOPNO() : opNo;
            //lotNo也是plotNo
            String lotNo = vehicleLotnoDetailList.get(0).getLOTNO().replaceAll("\uFEFF", "");
            // 和建男确认过，queryOpNo和lotNo如果为空，说明还没进过站，管控点7直接跳过
            if(ObjectUtils.isNotEmpty(queryOpNo) && ObjectUtils.isNotEmpty(lotNo)){
                List<QuerySNnoByLotnoOpnoVehicleNoDetail> querySNnoByLotnoOpnoVehicleNoDetails = querySNnoByLotnoOpnoVehicleNoMapper.querySNnoByLotnoOpnoVehicleNo(carrierNo, lotNo, queryOpNo);
                log.debug(baseLog + "进站-管控点7:querySNnoByLotnoOpnoVehicleNoMapper执行时间：" + LogUtil.splitTime(stopWatch));
                if(ObjectUtils.isNotEmpty(querySNnoByLotnoOpnoVehicleNoDetails)){
                    log.debug(baseLog + "管控点7: sn重复，该载具内的sn ({}) 已经进过站了", String.join(",", querySNnoByLotnoOpnoVehicleNoDetails.stream().map(x -> x.getPRSN()).collect(Collectors.toList())));
                    return ResponseDTO.build(ResponseCode.CHECKIN_ERROR_CONTROL_POINT_7);
                }
                log.debug(baseLog + "进站-管控点7执行时间：" + LogUtil.splitTime(stopWatch));
            }
        } else {
            // 进站：管控点6
            log.debug(baseLog + "进站-管控点6: 系统中找不到载具绑定过的SN信息，请先绑定SN数据");
            return ResponseDTO.build(ResponseCode.CHECKIN_ERROR_CONTROL_POINT_6);
        }

        log.debug(baseLog + "进站-管控点2执行时间：" + LogUtil.splitTime(stopWatch));
        // 进站-管控点4:
        //查询载具状态
        /*List<QueryVehicleStatusDetail> queryVehicleStatusDetailList = queryVehicleStatusMapper.queryVehicleStatus(carrierNo);
        log.debug(baseLog + "进站-管控点4：queryVehicleStatusMapper执行时间：" + LogUtil.splitTime(stopWatch));
        if (queryVehicleStatusDetailList.size() > 0){
            // 判断生产批是否存在于MES：plot_no为【获取载具绑定的生产批】中的LOTNO栏位
            String prodStatus = queryVehicleStatusDetailList.get(0).getPRODSTATUS().trim();
            if(ObjectUtils.isNotEmpty(prodStatus) && !prodStatus.equals("Q")){
                log.debug(baseLog + "进站-管控点4: 载具状态不是Q状态");
                return ResponseDTO.build(ResponseCode.CHECKIN_ERROR_CONTROL_POINT_4);
            }
        }
        log.debug(baseLog + "进站-管控点4执行时间：" + LogUtil.splitTime(stopWatch));*/
        //进站-管控点8: 这里的opNo也只能用接口入参的了，因为上一个sql没有返回opNo。
        List<QueryVehicleStatusDetail> queryVehicleStatusDetailList = queryVehicleStatusMapper.queryVehicleStatus(carrierNo);
        log.debug(baseLog + "进站-管控点8：queryVehicleStatusMapper执行时间：" + LogUtil.splitTime(stopWatch));
        if (ObjectUtils.isNotEmpty(queryVehicleStatusDetailList) && ObjectUtils.isNotEmpty(opNo)){
            List<QueryOpnoTimeSettingDetail> queryOpnoTimeSettingDetails = queryOpnoTimeSettingMapper.queryOpnoTimeSetting(opNo);
            log.debug(baseLog + "进站-管控点8：queryOpnoTimeSettingMapper执行时间：" + LogUtil.splitTime(stopWatch));
            if (ObjectUtils.isNotEmpty(queryOpnoTimeSettingDetails)){
                String isQTime = queryOpnoTimeSettingDetails.get(0).getISQTIME();
                String isPLLTime = queryOpnoTimeSettingDetails.get(0).getISPLLTIME();
                String timeInfo = queryVehicleStatusDetailList.get(0).getTIMEINFO();
                double qTime = queryOpnoTimeSettingDetails.get(0).getQTIME();
                double pllTime = queryOpnoTimeSettingDetails.get(0).getPLLTIME();
                if("1".equals(isQTime)){
                    long diffMin = DateUtils.diffMin(DateUtils.getNowDate(),DateUtils.parseDate(timeInfo));
                    if(diffMin > qTime){
                        log.debug(baseLog + "进站-管控点8: Q-TIME检查项超出标准时间");
                        return ResponseDTO.build(ResponseCode.CHECKIN_ERROR_CONTROL_POINT_8);
                    }
                }
                if("1".equals(isPLLTime)){
                    long diffDays = DateUtils.diffDays(DateUtils.getNowDate(),DateUtils.parseDate(timeInfo));
                    if(diffDays > pllTime){
                        log.debug(baseLog + "进站-管控点8: Production lot lifetime control检查项超出标准时间");
                        return ResponseDTO.build(ResponseCode.CHECKIN_ERROR_CONTROL_POINT_8);
                    }
                }
            } else {
                // 和建男确认过，opNo有值时检查这段逻辑
                return ResponseDTO.build(ResponseCode.UNKNOWN_ERROR,"进站-管控点8: 作业站不存在");
            }

        }

        log.debug(baseLog + "进站-管控点8执行时间：" + LogUtil.splitTime(stopWatch));

        //管控点13
        List<QueryVehicleBindProductionSNInfoDetail> queryVehicleBindProductionSNInfoDetails = queryVehicleBindProductionSNInfoMapper.queryVehicleBindProductionSNInfo(carrierNo);
        log.debug(baseLog + "进站-管控点13：queryVehicleBindProductionSNInfoMapper执行时间：" + LogUtil.splitTime(stopWatch));
        if(ObjectUtils.isNotEmpty(queryVehicleBindProductionSNInfoDetails)){
            boolean isverify = true;
            String queryOpNo = queryVehicleBindProductionSNInfoDetails.get(0).getOPNO();
            if (ObjectUtils.isNotEmpty(queryOpNo) && "1".equals(queryVehicleBindProductionSNInfoDetails.get(0).getISVERIFY())){
                for (int i = 0; i < queryVehicleBindProductionSNInfoDetails.size(); i++) {
                    if(isverify && "0".equals(queryVehicleBindProductionSNInfoDetails.get(i).getVERIFY())){
                        isverify = false;
                    }
                }
            } else if (ObjectUtils.isEmpty(queryOpNo)) {
                //这里和建男确认过，第二个sql就是使用接口里的opNo，如果queryOpNo和opNo都为空，则返回成功
                if(ObjectUtils.isNotEmpty(opNo)){
                    List<QueryWhetherTestSNLengthDetail> queryWhetherTestSNLengthDetails = queryWhetherTestSNLengthMapper.queryWhetherTestSNLength(opNo);
                    if(ObjectUtils.isNotEmpty(queryWhetherTestSNLengthDetails) && "1".equals(queryWhetherTestSNLengthDetails.get(0).getISVERIFY())){
                        for (int i = 0; i < queryVehicleBindProductionSNInfoDetails.size(); i++) {
                            if(isverify && queryVehicleBindProductionSNInfoDetails.get(i).getPRODUCTIONSN().length() != Integer.parseInt(queryWhetherTestSNLengthDetails.get(0).getISVERIFYVALUE())){
                                isverify = false;
                            }
                        }
                    }
                }
            }
            if (!isverify){
                log.debug(baseLog + "进站-管控点13: 该载具内SN长度不符合作业站 {} 设定SN长度，载具 {} 进站失败！",opNo,carrierNo);
                return ResponseDTO.build(ResponseCode.CHECKIN_ERROR_CONTROL_POINT_13);
            }
        } else {
            log.debug(baseLog + "进站-管控点13: 该载具未绑定任何产品");
            return ResponseDTO.build(ResponseCode.CHECKIN_ERROR_CONTROL_POINT_13);
        }

        log.debug(baseLog + "进站-管控点13执行时间：" + LogUtil.splitTime(stopWatch));
        return ResponseDTO.build(ResponseCode.CHECKIN_SUCCESS);
    }

}
