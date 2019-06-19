package com.ext.huya.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ext.huya.callback.Callback;
import com.ext.huya.dto.ItemData;
import com.ext.huya.dto.MessageData;
import com.ext.huya.dto.ResponseDto;
import com.ext.huya.dto.VipBannerData;
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
        JSONObject jsonObject = JSON.parseObject(text);
        //ResponseDto responseDto = JSON.parseObject(text, new TypeReference<ResponseDto>() {});
        if (200 == Integer.valueOf(jsonObject.get("statusCode").toString())) {
            switch (jsonObject.get("notice").toString()) {
                case "getVipEnterBannerNotice":
                    VipBannerData vipBannerData = JSON.parseObject(jsonObject.get("data").toString(), new TypeReference<VipBannerData>() {
                    });
                    //System.out.println("会员消息：" + vipBannerData.getUserNick());
                    callback.vipMessage(vipBannerData);
                    break;
                case "getMessageNotice":
                    MessageData messageData = JSON.parseObject(jsonObject.get("data").toString(), new TypeReference<MessageData>() {
                    });
                    //System.out.println("弹幕消息：" + messageData.getSendNick());
                    callback.message(messageData);
                    break;
                case "getSendItemNotice":
                    ItemData itemData = JSON.parseObject(jsonObject.get("data").toString(), new TypeReference<ItemData>() {
                    });
                    //System.out.println("礼物消息：" + itemData.getItemName());
                    callback.itemMessage(itemData);
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

}
