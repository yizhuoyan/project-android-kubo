package com.liuyongmei.kubo.model.datamodel;

/**
 * 异步消息，用于线程间传递
 * Created by Administrator on 2017/3/28 0028.
 */

public class SyncMessage {
    public static final int LOGIN_SUCCEED=0Xffff;
    public static final int LOGIN_FAILED=0Xfffe;
    public static final int LOGINING=0Xfffd;
    //读数据异常
    public static final int READ_EXCEPTION=0Xfff1;
    //写数据异常
    public static final int WRITE_EXCEPTION=0Xfff2;
    public static final int UNKNOW_EXCEPTION=0Xfff0;
    public int code;
    public String message;

    public SyncMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public SyncMessage(int code) {
        this(code,null);
    }

    public SyncMessage() {
    }

    @Override
    public String toString() {
        return "SyncMessage{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
