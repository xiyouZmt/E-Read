package com.zmt.e_read.Thread;

import android.os.Handler;
import android.os.Message;

import com.zmt.e_read.Utils.OkHttpUtils;

/**
 * Created by Dangelo on 2016/9/28.
 */
public class GetNewsData implements Runnable {

    private boolean getMore;
    private String url;
    private Handler handler;

    public GetNewsData(String url, Handler handler) {
        this.url = url;
        this.handler = handler;
    }

    public GetNewsData(boolean getMore, String url, Handler handler) {
        this.getMore = getMore;
        this.url = url;
        this.handler = handler;
    }

    @Override
    public void run() {
        OkHttpUtils okHttpUtils = new OkHttpUtils(url);
        String result = okHttpUtils.getNewsData();
        Message msg = new Message();
        msg.obj = result;
        handler.sendMessage(msg);
    }
}
