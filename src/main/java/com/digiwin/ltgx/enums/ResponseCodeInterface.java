package com.digiwin.ltgx.enums;

public interface ResponseCodeInterface {

    /**
     * 返回result
     * @return 错误码
     */
    String result();

    /**
     * 返回错误码
     * @return 错误码
     */
    String code();

    /**
     *
     * @return 错误描述
     */
    String message();

}
