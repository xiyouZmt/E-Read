package com.zmt.e_read.Thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zmt.e_read.Adapter.VideoAdapter;
import com.zmt.e_read.ViewHolder.ViewHolder;

/**
 * Created by MintaoZhu on 2016/11/27.
 */
public class DelayThread extends Thread {

    private final ViewHolder viewHolder;
    private Handler handler;
    private int videoLength;
    private int currentTime = 0;
    public  static boolean update = true;

    public DelayThread(ViewHolder viewHolder, Handler handler, int videoLength) {
        this.viewHolder = viewHolder;
        this.handler = handler;
        this.videoLength = videoLength;
    }

    public DelayThread(ViewHolder viewHolder, Handler handler, int videoLength, int currentTime) {
        this.viewHolder = viewHolder;
        this.handler = handler;
        this.videoLength = videoLength;
        this.currentTime = currentTime;
    }

    @Override
    public void run() {
        synchronized (viewHolder){
            while (update){
                Message msg = handler.obtainMessage();
                msg.what = VideoAdapter.UPDATE;
                msg.obj = viewHolder;
                msg.arg1 = currentTime;
                handler.sendMessage(msg);
                if(currentTime == videoLength){
                    break;
                }
                try {
                    sleep(1000);
                    currentTime ++;
                } catch (InterruptedException e) {
                    Log.e("sleep error", e.toString());
                }
            }
            VideoAdapter.currentTime = currentTime;
        }
    }
}