package com.liuyongmei.kubo.model;

import com.liuyongmei.kubo.model.datamodel.SyncMessage;

/**
 * 异步消息接收器
 * Created by Administrator on 2017/3/28 0028.
 */

public interface SyncMessageListener {
        void onReceive(SyncMessage message);
}
