package com.ext.huya.kit;

public enum Func {
    MESSAGE_NOTICE("getMessageNotice"),
    ITEM_NOTICE("getSendItemNotice"),
    TV_NOTICE("getOnTVAwardNotice"),
    VIP_BANNER_NOTICE("getVipEnterBannerNotice");

    private String func;

    Func(String func) {
        this.func = func;
    }

    public String getFunc() {
        return func;
    }
}
