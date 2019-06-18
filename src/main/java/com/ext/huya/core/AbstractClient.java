package com.ext.huya.core;

import java.util.stream.Stream;

public abstract class AbstractClient {
    protected long iat;
    protected long exp;
    protected String appId;
    protected String secret;


    protected String urlAssembly(String url, Object... param) {
        StringBuilder sb = new StringBuilder(url);
        Stream.of(param).forEach(k -> {
            sb.replace(sb.indexOf("{"), sb.indexOf("}") + 1, String.valueOf(k));

        });
        return sb.toString();
    }


}
