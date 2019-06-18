package com.ext.huya.callback;

public interface Event {

    void send(io.netty.channel.Channel channel , Object obj );
}
