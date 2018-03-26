package com.xzper.android.server.website;

import android.content.Context;

import com.example.xzper.server.R;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.xzper.android.server.Server;
import com.yanzhenjie.andserver.view.OkView;
import com.yanzhenjie.andserver.view.View;
import com.yanzhenjie.andserver.website.WebSite;

import org.apache.httpcore.Header;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.protocol.HttpContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xzper on 2018/3/25.
 */

public class AndroidRedirectWebsite implements WebSite {

    private static String key = "App";
    private static SyncHttpClient client = new SyncHttpClient();


    private Map<String, String> apps = new HashMap<>();
    private int serverPort;

    public AndroidRedirectWebsite(Context context, int serverPort) {
        this.registerApps(context);
        this.serverPort = serverPort;
    }

    private void registerApps(Context context) {
        String[] appsArray = context.getResources().getStringArray(R.array.apps);
        for (int i = 0; i < appsArray.length; i++) {
            String[] kv = appsArray[i].split(",");
            apps.put(kv[0], kv[i]);
        }
    }

    @Override
    public boolean intercept(HttpRequest request, HttpContext context) throws HttpException, IOException {
        Header[] headers = request.getHeaders(key);
        if (headers.equals(null) || headers.length == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void handle(HttpRequest request, final HttpResponse response, HttpContext context) throws HttpException, IOException {
        String uri = request.getRequestLine().getUri();
        uri = "http://" + Server.getAddress().getHostAddress() + ":" + serverPort + uri;
        Header[] headers = request.getHeaders(key);
        String appName = headers[0].getValue();
        String port = apps.get(appName);
        String redirectUri = uri.replace(String.valueOf(serverPort), port);

        if (request.getRequestLine().getMethod().toLowerCase().equals("get")) {
            client.get(redirectUri, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, String s, Throwable throwable) {
                    AndroidRedirectWebsite.this.setResult(response, new View(i, s));
                }

                @Override
                public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, String s) {
                    AndroidRedirectWebsite.this.setResult(response, new OkView(s));
                }
            });
        } else {
            this.setResult(response, new OkView(""));
        }
    }

    private void setResult(HttpResponse response, View view) {
        response.setStatusCode(view.getHttpCode());
        response.setEntity(view.getHttpEntity());
        response.setHeaders(view.getHeaders());
    }
}
