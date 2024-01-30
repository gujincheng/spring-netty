package com.digiwin.ltgx.enums;

/**
 * 错误码集合
 */
public enum ResponseCode implements ResponseCodeInterface {

    CHECKIN_ERROR_CONTROL_POINT_1("error","306", "系统Q任务数量小于载具内的sn的数据"),
    CHECKIN_ERROR_CONTROL_POINT_2("error","305", "批次号不符：载具内的批次号与mes现行的任务批次不一样"),
    CHECKIN_ERROR_CONTROL_POINT_3("error","304", "没有设定上机人员"),
    CHECKIN_ERROR_CONTROL_POINT_4("error","302", "载具状态不是Q状态"),
    CHECKIN_ERROR_CONTROL_POINT_5("error","303", "载具内的工序号和当前要进站的工序号不一样"),
    CHECKIN_ERROR_CONTROL_POINT_6("error","301", "载具上没有绑定SN"),
    CHECKIN_ERROR_CONTROL_POINT_7("error","310", "sn重复，该载具内的sn已经进过站了"),
    CHECKIN_ERROR_CONTROL_POINT_8("error","311", "Q-time时间管控了（CT-TIME/CycleTime/P-TIME等类似）"),
    CHECKIN_ERROR_CONTROL_POINT_9("error","312", "被风险标记过的"),
    CHECKIN_ERROR_CONTROL_POINT_10("error","307", "设备点检"),
    CHECKIN_ERROR_CONTROL_POINT_11("error","308", "设备稼动状态不对不能做进出站"),
    CHECKIN_ERROR_CONTROL_POINT_12("error","313", "机时异常"),
    CHECKIN_ERROR_CONTROL_POINT_13("error","309", "sn长度不符合可进站的sn长度"),
    CHECKIN_ERROR_CONTROL_POINT_14("error","314", "辅料有效期管控"),


    CHECKOUT_ERROR_CONTROL_POINT_1("error","401", "载具状态不是R状态"),
    CHECKOUT_ERROR_CONTROL_POINT_2("error","402", "出站载具工序与设备工序不一致"),
    CHECKOUT_ERROR_CONTROL_POINT_3("error","403", "出站任务批次与载具批次不符，或者没有对应出站批次R任务"),
    CHECKOUT_ERROR_CONTROL_POINT_4("error","405", "HR镀膜BD面锅次号管控"),
    CHECKOUT_ERROR_CONTROL_POINT_5("error","404", "CT-TIME 管控"),
    CHECKOUT_ERROR_CONTROL_POINT_6("error","406", "被风险标记过的"),

    CHECKBINDING_ERROR_CONTROL_POINT_1("error","804", "sn数量上传大于载具上限数量，或者现场使用载具未录入MES系统"),
    CHECKBINDING_ERROR_CONTROL_POINT_2("error","803", "载具的二维码未获取到"),
    CHECKBINDING_ERROR_CONTROL_POINT_3("error","801", "载具为R状态，无法进行绑定"),
    CHECKBINDING_ERROR_CONTROL_POINT_4("error","802", "载具需要绑定的批次号未获取到"),
    CHECKBINDING_ERROR_CONTROL_POINT_5("error","805", "长条sn00开头不能和10开头的绑定，棱镜sn7c开头不能和88开头绑定"),
    CHECKBINDING_ERROR_CONTROL_POINT_6("error","806", "长条sn00和棱镜sn7c开头只能绑定V3开头批次，长条sn10和棱镜sn88开头的只能绑定V4开头的"),
    CHECKBINDING_ERROR_CONTROL_POINT_7("error","807", "接口绑定，绑定sn为空时也报绑定版本不一致。"),
    CHECKBINDING_ERROR_CONTROL_POINT_8("error","808", "在载具绑码上传MES时候（最大256颗SN），增加SN校验黑名单功能，当SN出现黑名单内，绑码提示“存在有黑名单的sn，绑定失败”"),
    CHECKBINDING_ERROR_CONTROL_POINT_9("error","809", "该载具状态已标记为风险，无法进出站"),
    CHECKBINDING_ERROR_CONTROL_POINT_10("error","810", "系统中找不到载具信息"),

    CHECKCLEANUP_ERROR_CONTROL_POINT_1("error","702", "载具的二维码未获取到"),
    CHECKCLEANUP_ERROR_CONTROL_POINT_2("error","701", "载具为R状态，无法清空"),

    CHECKQUERY_ERROR_CONTROL_POINT_1("error","901", "当前载具没有任何绑定信息"),

    CHECKIN_SUCCESS("ok","200", "进站成功"),
    CHECKOUT_SUCCESS("ok","200", "出站成功"),
    CHECKBINDING_SUCCESS("ok","200", "绑定成功"),
    CHECKCLEANUP_SUCCESS("ok","200", "载具清空成功"),
    CHECKQUERY_SUCCESS("ok","200", "%s"),

    UNKNOWN_ERROR("error","9999", "%s"),
    PARSE_ERROR("error","-500", "接口传入不是标准JSON");
    private final String result;
    private final String code;
    private final String msg;

    ResponseCode(String result, String code, String msg) {
        this.result = result;
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String result() {
        return this.result;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.msg;
    }

}
