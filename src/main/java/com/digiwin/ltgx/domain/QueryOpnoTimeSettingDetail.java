package com.digiwin.ltgx.domain;

import lombok.Data;


@Data
public class QueryOpnoTimeSettingDetail {
    //ISQ_TIME, Q_TIME, ISPLLTIME, PLLTIME, ISCYCLETIME, CYCLETIME, OPNO
    private String ISQTIME;
    private double QTIME;
    private String ISPLLTIME;
    private double PLLTIME;
    private String ISCYCLETIME;
    private double CYCLETIME;
    private String ISPOT;
    private String OPNO;

}
