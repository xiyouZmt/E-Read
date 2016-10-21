package com.zmt.e_read.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

/**
 * Created by MintaoZhu on 2016/10/21.
 */
public class ThunderUtils {

    private final String PACKAGE_NAME = "com.xunlei.downloadprovider";
    private Context context;
    public static ThunderUtils thunderUtils;

    public static ThunderUtils getInstance(Context context) {
        thunderUtils = new ThunderUtils(context);
        return thunderUtils;
    }

    public ThunderUtils(Context context){
        this.context = context;
    }

    public boolean isInstalled(){
        try {
            context.getPackageManager().getApplicationInfo(PACKAGE_NAME, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("xunlei error", e.toString());
            return false;
        }
    }

    public void downloadFile(String url){
        if(isInstalled()){
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        }
    }
}
