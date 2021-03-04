package com.work.wb.util.model;

/**
 * Created by bing.wang on 2018/1/24.
 */

public class DataDetailModel {
    private String key;
    private String value;

    public DataDetailModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
