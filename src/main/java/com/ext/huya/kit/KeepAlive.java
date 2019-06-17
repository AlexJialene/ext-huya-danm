package com.ext.huya.kit;

import io.netty.channel.Channel;

public class KeepAlive extends Thread {
    private Channel channel;

    public KeepAlive(Channel ch) {
        this.channel = ch;
    }

    @Override
    public void run() {
        super.run();
    }


}
