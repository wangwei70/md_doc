//package com.better.error;
//
//import com.asiainfo.ahweb.enums.ErrorCodeEnums;
//
///**
// * @Author: JeanRiver
// * @Description:
// * @Date Created at 17:23 2022/5/24
// * @Modified By:
// */
//public class DefinedException extends RuntimeException {
//
//    protected Integer errorCode;
//    protected String errorMsg;
//
//    public DefinedException(){
//
//    }
//    public DefinedException(Integer errorCode, String errorMsg) {
//        this.errorCode = errorCode;
//        this.errorMsg = errorMsg;
//    }
//
//    public DefinedException(ErrorCodeEnums errorCodeEnums) {
//        this.errorCode = errorCodeEnums.getErrorCode();
//        this.errorMsg = errorCodeEnums.getErrMsg();
//    }
//
//    public DefinedException(ErrorCodeEnums errorCodeEnums, String errorMsg) {
//        this.errorCode = errorCodeEnums.getErrorCode();
//        this.errorMsg = errorMsg;
//    }
//
//    public Integer getErrorCode() {
//        return errorCode;
//    }
//
//    public void setErrorCode(Integer errorCode) {
//        this.errorCode = errorCode;
//    }
//
//    public String getErrorMsg() {
//        return errorMsg;
//    }
//
//    public void setErrorMsg(String errorMsg) {
//        this.errorMsg = errorMsg;
//    }
//}
