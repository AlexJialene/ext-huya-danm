package com.ext.huya;

import com.ext.huya.callback.Callback;
import com.ext.huya.core.AbstractClient;
import com.ext.huya.core.SocketClient;
import com.ext.huya.kit.Func;
import com.ext.huya.kit.WSLink;

import java.net.URISyntaxException;
import java.util.stream.Stream;

public class Example {

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        AbstractClient abstractClient = new SocketClient("", "", new Callback() {
            @Override
            public void message(Object msg) {

            }

            @Override
            public void itemMessage(Object msg) {

            }

            @Override
            public void vipMessage(Object msg) {

            }
        });

        String token = ((SocketClient) abstractClient).getToken();


        ((SocketClient) abstractClient).addFunc(Func.ITEM_NOTICE);
        //((SocketClient) abstractClient).addFunc(Func.MESSAGE_NOTICE);
        ((SocketClient) abstractClient).start(WSLink.CONNECT_WS  , "518512");

        //Stream.of(((SocketClient) abstractClient).getParams()).forEach(System.out::println);
        //System.out.println(token);
    }
}
