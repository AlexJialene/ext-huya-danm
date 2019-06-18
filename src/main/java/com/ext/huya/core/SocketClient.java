package com.ext.huya.core;

import com.alibaba.fastjson.JSON;
import com.ext.huya.callback.Callback;
import com.ext.huya.callback.Event;
import com.ext.huya.dto.Command;
import com.ext.huya.kit.Func;
import com.ext.huya.kit.JwtKit;
import com.ext.huya.kit.KeepAlive;
import com.ext.huya.kit.WSLink;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Stream;

public class SocketClient extends AbstractClient implements Event {
    private String token;
    private String host;
    private int port;
    private boolean isSsl = false;
    private Callback callback;
    private String[] params;
    private int off;

    public SocketClient(String appId, String secret, Callback callback) {
        this.appId = appId;
        this.secret = secret;
        this.callback = callback;
        this.iat = System.currentTimeMillis() / 1000;
        this.exp = iat + 600;

        this.token = JwtKit.createH256Token("{\"iat\":" + this.iat + ",\"exp\":" + this.exp + ",\"appId\":\"" + this.appId + "\"}", this.secret);
    }

    public Channel start(WSLink link, String roomId) throws URISyntaxException, InterruptedException {
        String requestUrl = getRequestUrl(link, roomId);
        System.out.println(requestUrl);
        URI uri = new URI(requestUrl);
        initRequest(uri);

        EventLoopGroup group = new NioEventLoopGroup();

        final WebSocketClientHandler handler = new WebSocketClientHandler(
                WebSocketClientHandshakerFactory.newHandshaker(
                        uri,
                        WebSocketVersion.V13,
                        null,
                        false,
                        new DefaultHttpHeaders()),
                this.callback,
                link
        );

        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws SSLException {
                        ChannelPipeline p = ch.pipeline();
                        if (isSsl()) {
                            SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
                            p.addLast(sslCtx.newHandler(ch.alloc(), getHost(), getPort()));
                        }
                        p.addLast(new HttpClientCodec(), new HttpObjectAggregator(8192), handler);
                    }
                });

        Channel ch = b.connect(uri.getHost(), getPort()).sync().channel();
        handler.setHandshaker(ch);
        handler.handshakeFuture().sync();

        //keepalive
        new KeepAlive(ch).start();
        this.send(ch, new Command(this.params == null ? new String[]{Func.MESSAGE_NOTICE.getFunc()} : this.params));
        return ch;
    }

    private void initRequest(URI uri) {
        String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
        setHost(uri.getHost() == null ? "127.0.0.1" : uri.getHost());

        if (uri.getPort() == -1) {
            if ("ws".equalsIgnoreCase(scheme)) {
                setPort(80);
            } else if ("wss".equalsIgnoreCase(scheme)) {
                setPort(443);
            } else {
                setPort(-1);
            }
        } else {
            setPort(uri.getPort());
        }
        if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
            System.err.println("Only WS(S) is supported.");
            return;
        }
        setSsl("wss".equalsIgnoreCase(scheme));
    }

    private String getRequestUrl(WSLink link, String roomId) {
        return urlAssembly(link.getLink(), roomId, appId, iat, exp, token);
    }

    public String getToken() {
        return this.token;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isSsl() {
        return isSsl;
    }

    public String[] getParams() {
        return params;
    }

    public SocketClient setFunc(Func... func) {
        params = new String[4];
        Stream.iterate(0, i -> i + 1).limit(func.length).forEach(i -> {
            if (4 <= i) {
                return;
            }
            this.params[i] = func[i].getFunc();
            this.off = i;
        });
        return this;
    }

    @Deprecated
    public SocketClient addFunc(Func func) {
        if (null == this.params) {
            this.params = new String[4];
            this.off = 0;
        }
        if (this.off == 3) {
            System.out.println("The parameter array has reached its maximum！");
            return this;
        }
        this.params[this.off + 1] = func.getFunc();
        this.off++;

        return this;
    }

    public void cleanFunc() {
        this.params = new String[4];

    }

    protected void setHost(String host) {
        this.host = host;
    }

    protected void setPort(int port) {
        this.port = port;
    }

    protected void setSsl(boolean ssl) {
        isSsl = ssl;
    }

    @Override
    public void send(Channel channel, Object obj) {
        TextWebSocketFrame frame = new TextWebSocketFrame(JSON.toJSONString(obj));
        channel.writeAndFlush(frame).addListener(channelFuture -> {
            if (channelFuture.isSuccess()) {
                System.out.println("command send =>");
            }
        });
    }
}
