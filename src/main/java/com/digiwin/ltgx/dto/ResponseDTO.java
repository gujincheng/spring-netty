package com.digiwin.ltgx.dto;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.digiwin.ltgx.domain.QueryVehicleBindSNDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.digiwin.ltgx.enums.ResponseCodeInterface;

import java.util.List;

/**
 * 响应信息主体
 */
@Data
@AllArgsConstructor
public class ResponseDTO<T> {

    private String code;
    private String result;
    private T msg;


    public static <T> JSONObject build(ResponseCodeInterface code, Object... args) {
        return (JSONObject) JSONObject.toJSON(new ResponseDTO<>(code.code(),code.result(), String.format(code.message(), args)));
    }

    public static <T> JSONObject build(ResponseCodeInterface code, List<QueryVehicleBindSNDetail> details) {
        String detailJson = JSON.toJSONString(details, SerializerFeature.WriteNullStringAsEmpty);
        JSONObject message = new JSONObject();
        message.put("detail", JSON.parseArray(detailJson, JSONObject.class));
        return (JSONObject) JSONObject.toJSON(new ResponseDTO<>(code.code(), code.result(), message));
    }
}

