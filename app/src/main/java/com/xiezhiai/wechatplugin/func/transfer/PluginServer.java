package com.xiezhiai.wechatplugin.func.transfer;

import android.app.AndroidAppHelper;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.xiezhiai.wechatplugin.core.Config;
import com.xiezhiai.wechatplugin.utils.LogUtil;
import com.xiezhiai.wechatplugin.utils.PluginLoginManager;
import com.xiezhiai.wechatplugin.utils.network.NetworkUtility;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by shijiwei on 2018/11/29.
 *
 * @Desc:
 */
public class PluginServer extends Service {

    private static final String TAG = "PluginServer";
    public static final String SUFFIX = "\r\n";
    /* tcp server port */
    private final int TCP_PORT = 6666;
    /* */
    private static final int CHECK_TCP_SERVER_INTERVAL = 5 * 1000;
    /* The monitoring service is opened */
    private boolean isStartServer = false;
    private boolean isPutingMessageToQueue = false;
    private boolean TCP_SERVER_ENABLE = true;

    private ServerSocket mServer;
    private static HashMap<String, Socket> mClientSet = new HashMap<>();
    private StringBuilder buffer = new StringBuilder();
    private static PluginHandler pluginHandler;

    private ExecutorService mServerPolice = Executors.newCachedThreadPool();
    private static LinkedBlockingQueue<PluginMessasge> pluginMessasges = new LinkedBlockingQueue<>();

    private static PluginServer server;

    public PluginServer() {
        pluginHandler = new PluginHandler(this);
    }

    static {
        server = new PluginServer();
    }

    private static Handler mServerLooper = new Handler();
    private Runnable mServerStateRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isStartServer && TCP_SERVER_ENABLE) {
                startServer();
            }
            mServerLooper.postDelayed(this, CHECK_TCP_SERVER_INTERVAL);
        }
    };


    /**
     * send heart beat to client
     *
     * @param out
     * @param msg
     * @throws IOException
     */
    private static Handler mHeartBeatHandler = new Handler();
    private static Runnable mHeartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            tansferMessage(PluginMessasge.obtainHeartBeatMessage());
            mHeartBeatHandler.postDelayed(this, CHECK_TCP_SERVER_INTERVAL);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        isStartServer = false;
        TCP_SERVER_ENABLE = true;
        mServerLooper.removeCallbacks(mServerStateRunnable);
        mServerLooper.postDelayed(mServerStateRunnable, 0);

    }

    @Override
    public void onDestroy() {
        release();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 消息轮训机
     */
    private class MessageLooper implements Runnable {

        private Socket socket;
        private String host = "";

        public MessageLooper(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try {
                if (socket != null) {
                    host = socket.getInetAddress().getHostName();
                    while (socket != null && socket.isConnected() && !socket.isClosed() && !socket.isInputShutdown() && !socket.isOutputShutdown() && TCP_SERVER_ENABLE) {
                        int index = buffer.indexOf(SUFFIX);
                        if (index != -1) {
                            String _msg = buffer.substring(0, index);
                            buffer.delete(0, index + SUFFIX.length());
                            Message msg = pluginHandler.obtainMessage();
                            msg.obj = _msg;
                            pluginHandler.sendMessage(msg);
                        }
                    }
                }
            } catch (Exception e) {

            } finally {
                try {
                    if (socket != null) {
                        socket.close();
                        socket = null;
                        mClientSet.remove(host);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    LogUtil.e(TAG + "  ======= client host: " + host + " leave ===== MessageLooper ");
                }
            }
        }
    }

    /**
     * 服务端报文读操作
     */
    class ServerReader implements Runnable {

        private WeakReference<Socket> wr;
        private String host = "";

        public ServerReader(Socket client) {
            this.wr = new WeakReference<Socket>(client);
        }

        @Override
        public void run() {
            Socket client = wr.get();
            InputStream in = null;
            try {
                if (client != null) {
                    host = client.getInetAddress().getHostName();
                    in = client.getInputStream();
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) != -1 && client != null && !client.isClosed() && client.isConnected() && !client.isOutputShutdown() && !client.isInputShutdown()) {
                        buffer.append(new String(buf, 0, len));
                    }
                    LogUtil.e(TAG + " len = " + len);
                    logError(client);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                try {
                    if (in != null) {
                        in.close();
                        client.close();
                        client = null;
                        mClientSet.remove(host);
                        LogUtil.e(TAG + "  ======= client host: " + host + " leave ===== Write ");

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 服务端报文写操作
     */
    class ServerWriter implements Runnable {

        private WeakReference<Socket> wr;
        private String host = "";

        public ServerWriter(Socket client) {
            wr = new WeakReference<Socket>(client);
            mHeartBeatHandler.removeCallbacks(mHeartBeatRunnable);
            mHeartBeatHandler.post(mHeartBeatRunnable);
        }

        @Override
        public void run() {
            Socket client = wr.get();
            OutputStream outs = null;
            try {
                if (client != null) {
                    host = client.getInetAddress().getHostName();
                    outs = client.getOutputStream();

                    while (outs != null && client != null && !client.isClosed() && client.isConnected() && !client.isOutputShutdown() && !client.isInputShutdown() && TCP_SERVER_ENABLE) {
                        PluginMessasge msg = pluginMessasges.poll();
                        if (msg == null) continue;
                        outs.write(msg.toJSONString().getBytes());
                        outs.flush();
                    }
                    logError(client);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (outs != null) {
                        outs.close();
                        client.close();
                        client = null;
                        mClientSet.remove(host);
                        LogUtil.e(TAG + "  ======= client host: " + host + " leave ===== Read");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 开启TCP服务
     */
    private void startServer() {

        mServerPolice.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    release();
                    mServer = new ServerSocket(TCP_PORT);
                    isStartServer = true;
                    TCP_SERVER_ENABLE = true;
                    LogUtil.e(TAG + "  ===== waiting for client : server host " + NetworkUtility.getHost() + " ======");
                    while (TCP_SERVER_ENABLE && isStartServer) {
                        Socket client = mServer.accept();
                        String host = client.getInetAddress().getHostName();
                        if (mClientSet.containsKey(host)) {
                            Socket socket = mClientSet.get(host);
                            socket.close();
                            socket = null;
                        }
                        mClientSet.put(host, client);
                        LogUtil.e(TAG + " ======= client host: " + host + " enter ======== " + AndroidAppHelper.currentProcessName() + " : " + Thread.currentThread().getName());
                        Thread reader = new Thread(new ServerReader(client));
                        reader.setDaemon(true);
                        reader.start();

                        Thread writer = new Thread(new ServerWriter(client));
                        writer.setDaemon(true);
                        writer.start();

                        Thread looper = new Thread(new MessageLooper(client));
                        looper.setDaemon(true);
                        looper.start();
                    }
                } catch (IOException e) {
                    LogUtil.e(TAG + " startServer Error: " + e.getMessage());
                    isStartServer = false;
                }
            }
        });

    }

    /**
     * 释放TCP资源
     */
    private void release() {
        try {
            if (mServer != null) {
                Iterator<String> iterator = mClientSet.keySet().iterator();
                while (iterator.hasNext()) {
                    String host = iterator.next();
                    Socket client = mClientSet.get(host);
                    if (client != null) {
                        client.close();
                        client = null;
                    }
                    iterator.remove();
                }
                mClientSet.clear();
                mServer.close();
                mServer = null;
                isStartServer = false;
                TCP_SERVER_ENABLE = false;

            }
        } catch (IOException e) {
        } finally {
            LogUtil.e(TAG + " ===== tcp server close ======");
        }
    }

    private void logError(Socket socket) {
        LogUtil.e(TAG + " socket = " + socket);
        if (socket != null) {
            LogUtil.e(TAG + " isClosed = " + socket.isClosed());
            LogUtil.e(TAG + " isConnected = " + socket.isConnected());
            LogUtil.e(TAG + " isOutputShutdown = " + socket.isOutputShutdown());
            LogUtil.e(TAG + " isInputShutdown = " + socket.isInputShutdown());
            LogUtil.e(TAG + " TCP_SERVER_ENABLE = " + TCP_SERVER_ENABLE);
        }
    }

    /**
     * 发送指令
     *
     * @param pMsg
     */
    public static void tansferMessage(PluginMessasge pMsg) {
        if (pMsg == null) return;
        try {
            Iterator<PluginMessasge> iterator = pluginMessasges.iterator();
            while (iterator.hasNext()) {
                PluginMessasge next = iterator.next();
                if (next != null && next.getAction() == pMsg.getAction()) {
                    pluginMessasges.remove(next);
                }
            }
            pluginMessasges.put(pMsg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static boolean pluginKeepAlive() {
        int size = mClientSet.size();
        LogUtil.e(TAG + " pluginKeepAlive size = " + size);
        return size != 0;
    }

    /**
     * 开启服务
     */
    public static void start() {
        if (!server.isStartServer) {
            server.onCreate();
        }
    }

    /**
     * 关闭服务
     */
    public static void stop() {
        server.onDestroy();
    }

}
