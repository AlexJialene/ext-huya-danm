package com.ext.huya;

import com.ext.huya.callback.Callback;
import com.ext.huya.core.AbstractClient;
import com.ext.huya.core.SocketClient;
import com.ext.huya.dto.ItemData;
import com.ext.huya.dto.MessageData;
import com.ext.huya.dto.VipBannerData;
import com.ext.huya.kit.Func;
import com.ext.huya.kit.WSLink;

import java.net.URISyntaxException;
import java.util.stream.Stream;

public class Example {

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        AbstractClient abstractClient = new SocketClient("", "", new Callback() {

            @Override
            public void message(MessageData msg) {
                //弹幕消息
            }

            @Override
            public void itemMessage(ItemData msg) {
                //礼物消息
            }

            @Override
            public void vipMessage(VipBannerData msg) {
                //贵族进场消息
            }
        });

        String token = ((SocketClient) abstractClient).getToken();


        //((SocketClient) abstractClient).addFunc(Func.VIP_BANNER_NOTICE);
        //((SocketClient) abstractClient).addFunc(Func.MESSAGE_NOTICE);
        ((SocketClient) abstractClient).setFunc(Func.ITEM_NOTICE, Func.MESSAGE_NOTICE, Func.VIP_BANNER_NOTICE);
        ((SocketClient) abstractClient).start(WSLink.CONNECT_WS, "518512");

        //Stream.of(((SocketClient) abstractClient).getParams()).forEach(System.out::println);
        //System.out.println(token);
    }
}
