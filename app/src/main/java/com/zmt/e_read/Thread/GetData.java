package com.zmt.e_read.Thread;

import android.os.Handler;
import android.os.Message;

import com.zmt.e_read.Module.Movie;
import com.zmt.e_read.Utils.OkHttpUtils;

/**
 * Created by Dangelo on 2016/9/28.
 */
public class GetData implements Runnable {

    private String url;
    private Handler handler;
    private String type = "";

    public GetData(String url, Handler handler, String type) {
        this.url = url;
        this.handler = handler;
        this.type = type;
    }

    @Override
    public void run() {
        OkHttpUtils okHttpUtils = new OkHttpUtils(url);
        String result;
        if(type.equals(Movie.TAG)){
            result = okHttpUtils.getMovieData();
        } else {
            result = okHttpUtils.getNewsData();
        }
        Message msg = new Message();
        msg.obj = result;
        handler.sendMessage(msg);
    }
}
