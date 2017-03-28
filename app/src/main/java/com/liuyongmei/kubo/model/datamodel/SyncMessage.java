package com.liuyongmei.kubo.model.datamodel;

/**
 * 异步消息，用于线程间传递
 * Created by Administrator on 2017/3/28 0028.
 */

public class SyncMessage {
    final public String type;
    final public short code;
    final public String message;

    public SyncMessage(String type,short code, String message) {
        this.type=type;
        this.code = code;
        this.message = message;
    }

    public SyncMessage(String type,short code) {
        this(type,code,null);
    }
    public SyncMessage(String type) {
        this(type,(short) 0,null);
    }


}
