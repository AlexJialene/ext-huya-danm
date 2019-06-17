package com.ext.huya.core;

import com.ext.huya.callback.Callback;
import com.ext.huya.kit.WSLink;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private final WebSocketClientHandshaker webSocketClientHandshaker;
    private ChannelPromise handshakeFuture;
    private final Callback callback;
    //private WSLink wsLink;

    public WebSocketClientHandler(WebSocketClientHandshaker webSocketClientHandshaker) {
        this(webSocketClientHandshaker, null);
    }

    public WebSocketClientHandler(WebSocketClientHandshaker webSocketClientHandshaker, Callback callback) {
        this.webSocketClientHandshaker = webSocketClientHandshaker;
        this.callback = callback;
        //this.wsLink = WSLink.MESSAGE_NOTICE;
    }

    public WebSocketClientHandler(WebSocketClientHandshaker webSocketClientHandshaker, Callback callback, WSLink wsLink) {
        this.webSocketClientHandshaker = webSocketClientHandshaker;
        this.callback = callback;
        //this.wsLink = wsLink;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    public void setHandshaker(Channel channel) {
        webSocketClientHandshaker.handshake(channel);
    }

    /*public void setWsLink(WSLink wsLink) {
        this.wsLink = wsLink;
    }*/

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (!webSocketClientHandshaker.isHandshakeComplete()) {
            FullHttpResponse f = (FullHttpResponse) msg;
            System.out.println(f.toString());
            webSocketClientHandshaker.finishHandshake(ch, (FullHttpResponse) msg);
            System.out.println("WebSocket Client connected!");
            handshakeFuture.setSuccess();
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.getStatus() + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            String text = textFrame.text();
            //System.out.println("WebSocket Client received message: " + text);
            //
            //ResponseData<Message> responseData = JSON.parseObject(text , new TypeReference<ResponseData<Message>>(){});
            //System.out.println(responseData.getData().getBadgeName());
            toCallback(text);
        } else if (frame instanceof PongWebSocketFrame) {
            System.out.println("WebSocket Client received pong");
        } else if (frame instanceof CloseWebSocketFrame) {
            System.out.println("WebSocket Client received closing");
            ch.close();
        }

    }

    private void toCallback(String text) {
        if (null == callback) {
            return;
        }
        System.out.println(text);
        //TODO
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

}
