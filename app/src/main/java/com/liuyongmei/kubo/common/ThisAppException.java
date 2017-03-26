package com.liuyongmei.kubo.common;

/**
 * Created by Administrator on 2017/3/25 0025.
 */

public class ThisAppException extends RuntimeException {
    public ThisAppException(String detailMessage) {
        super(detailMessage);
    }

    public ThisAppException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
