package com.ext.huya.callback;

import com.ext.huya.dto.ItemData;
import com.ext.huya.dto.MessageData;
import com.ext.huya.dto.VipBannerData;

public interface Callback {

    void message(MessageData msg);

    void itemMessage(ItemData msg);

    void vipMessage(VipBannerData msg);

}
