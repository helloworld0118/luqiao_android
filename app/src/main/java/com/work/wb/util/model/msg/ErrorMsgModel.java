package com.work.wb.util.model.msg;

import com.work.wb.util.model.UserModel;

/**
 * Created by bing.wang on 2018/1/21.
 */

public class ErrorMsgModel {
    private boolean success;
    private String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
