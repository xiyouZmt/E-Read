package com.zmt.e_read.Thread;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.zmt.e_read.Utils.ImageUtils;

/**
 * Created by MintaoZhu on 2016/10/22.
 */
public class GetBitmap implements Runnable {

    private Context context;
    private String imageUrl;
    private Handler handler;

    public GetBitmap(Context context, String imageUrl, Handler handler) {
        this.context = context;
        this.imageUrl = imageUrl;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            /**
             * 保存图片
             */
            if(imageUrl != null){
                Bitmap bitmap = Glide.with(context).load(imageUrl).asBitmap()
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                if(bitmap != null){
                    if(ImageUtils.saveImage(bitmap, imageUrl)){
                        handler.sendEmptyMessage(0x000);
                    } else {
                        handler.sendEmptyMessage(0x001);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("get bitmap error", e.toString());
        }
    }
}
