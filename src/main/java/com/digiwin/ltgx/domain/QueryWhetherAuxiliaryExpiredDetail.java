package com.digiwin.ltgx.domain;

import lombok.Data;


@Data
public class QueryWhetherAuxiliaryExpiredDetail {
    //
    private String SHELFLIFE;
    private String MATERIALNO;
    private String EQUIPMENTNO;
    private String STARTDATE;
    private String STARTMONTH;
    private String EXPIREDTIMES;
    private String ISEXPIRED;
}
