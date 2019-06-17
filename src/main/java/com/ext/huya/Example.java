package com.ext.huya;

import com.ext.huya.callback.Callback;
import com.ext.huya.core.AbstractClient;
import com.ext.huya.core.SocketClient;

public class Example {

    public static void main(String[] args) {
        AbstractClient abstractClient = new SocketClient(123123, "123", new Callback() {
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
        System.out.println(token);
    }
}
