package com.xuecheng.framework.model.response;

import lombok.ToString;

/**
 * @Author: mrt.
 * @Description:
 * @Date:Created in 2018/1/24 18:33.
 * @Modified By:
 */

@ToString
public enum CommonCode implements ResultCode{

    SUCCESS(true,10000,"操作成功！"),
    FAIL(false,11111,"操作失败！"),
    UNAUTHENTICATED(false,10001,"此操作需要登陆系统！"),
    UNAUTHORISE(false,10002,"权限不足，无权操作！"),
    SERVER_ERROR(false,99999,"抱歉，系统繁忙，请稍后重试！"),
    INVALID_PARAM(false, 10003, "参数错误"),

    //cms page页面
    CMS_GENERATE_MODELISNULL(false,10101,"page页面模型为空"),
    CMS_GENERATE_DATAISNULL(false,10102,"page模型数据为空"),
    CMS_GENERATE_HTMLISNULL(false,10103,"page页面为空"),
    CMS_GENERATE_PAGE_NOT_EXIST(false,10104,"page不存在"),
//    private static ImmutableMap<Integer, CommonCode> codes ;
    //cms site 站点
    CMS_SITE_NOT_EXIST(false,10201,"site站点不存在");
    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private CommonCode(boolean success,int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }
    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }


}
