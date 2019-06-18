package com.ext.huya.kit;

public enum WSLink {

    CONNECT_WS(
            "ws://ws-apiext.huya.com/index.html?do=comm&roomId={}&appId={}&iat={}&exp={}&sToken={}"
    );

//    CONNECT_HTTPS(
//            "https://open-apiext.huya.com/channel/index?do=getChannelInfoByRoom&roomId={}&appId={}&iat={}&exp={}&sToken={}"
//    );

    private String link;

    WSLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
