package com.zmt.e_read.Utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by MintaoZhu on 2016/10/22.
 */
public class ImageUtils {

    public static final String rootPath = Environment.getExternalStorageDirectory() + "/";
    public static final String fileDir = rootPath + "E-Read/image";
    public static final String fileSuffix = ".jpeg";

    public static boolean saveImage(Bitmap bitmap, String url){
        File file = new File(fileDir);
        if(!file.exists()){
            file.mkdirs();
        }
        String imageName = MD5Utils.getInstance(url).hashKeyFromUrl() + fileSuffix;
        File imageFile = new File(file, imageName);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return true;
        } catch (IOException e) {
            Log.e("save image error", e.toString());
            return false;
        }
    }
}
