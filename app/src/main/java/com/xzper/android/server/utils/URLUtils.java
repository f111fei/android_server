package com.xzper.android.server.utils;

import com.yanzhenjie.andserver.util.HttpRequestParser;

import org.apache.httpcore.HttpRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by xzper on 2018-03-24.
 */

public class URLUtils {
    public static Map<String, String> parseParams(HttpRequest request) {
        Map<String, String> params = null;
        try {
            params = HttpRequestParser.parseParams(request);
            for (String key : params.keySet()) {
                params.put(key, URLDecoder.decode(params.get(key), "UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return params;
    }
}
