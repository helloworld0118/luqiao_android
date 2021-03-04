package com.work.wb.util.model.msg;

import com.work.wb.util.model.UserModel;

/**
 * Created by bing.wang on 2018/1/21.
 */

public class UserMsgModel {
    private boolean success;
    private UserModel msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UserModel getMsg() {
        return msg;
    }

    public void setMsg(UserModel msg) {
        this.msg = msg;
    }
}
