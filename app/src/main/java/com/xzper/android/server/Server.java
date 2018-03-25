package com.xzper.android.server;

import android.content.Context;

import com.example.xzper.server.R;
import com.xzper.android.server.utils.NetUtils;
import com.xzper.android.server.website.AndroidRedirectWebsite;
import com.yanzhenjie.andserver.AndServer;

import java.net.InetAddress;


/**
 * Created by xzper on 2018/2/11.
 */

public class Server {

    public static boolean started = false;

    private static InetAddress address;
    private static Context context;

    public static InetAddress getAddress() {
        return address;
    }

    public static Context getContext() {
        return context;
    }

    public static String start(Context context) {
        Server.context = context;
        int port = Integer.parseInt(context.getString(R.string.server_port));
        InetAddress address = NetUtils.getLocalIPAddress();
        com.yanzhenjie.andserver.Server server = AndServer.serverBuilder()
                .inetAddress(address)
                .port(port)
                .website(new AndroidRedirectWebsite(context, port))
                .build();

        server.startup();
        Server.started = true;
        Server.address = address;
        return address.getHostAddress() + ":" + port;
    }
}
