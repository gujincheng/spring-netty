package com.digiwin.ltgx.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class VehicleLotnoDetail {
    //a.opno, a.LOTNO,A.VehicleNo,E.VehicleName,E.VehicleCapacity,A.production_sn,A.production_location ,
    // A.production_width,A.USER_DEFINED01,A.USER_DEFINED02,E.risk_status
    private String OPNO;
    private String LOTNO;
    private String VEHICLENO;
    private String VEHICLENAME;
    private String VEHICLECAPACITY;
    private String PRODUCTIONSN;
    private String PRODUCTIONLOCATION;
    private String PRODUCTIONWIDTH;
    private String USERDEFINED01;
    private String USERDEFINED02;
    private String PRODSTATUS;
    private String BASELOTNO;
    private String RISKSTATUS;

}
