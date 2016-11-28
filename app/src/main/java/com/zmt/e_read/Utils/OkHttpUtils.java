package com.zmt.e_read.Utils;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Dangelo on 2016/9/28.
 */
public class OkHttpUtils {

    private String url;

    public OkHttpUtils(String url) {
        this.url = url;
    }

    public String getNewsData(){
        StringBuilder builder = new StringBuilder();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setFollowRedirects(false);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
//                BufferedReader reader = new BufferedReader(response.body().charStream());
//                String line;
//                while ((line = reader.readLine()) != null){
//                    builder.append(line);
//                }
//                reader.close();
                byte [] bytes = response.body().bytes();
                builder.append(new String(bytes));
                return builder.toString();
            } else {
                return "server error";
            }
        } catch (IOException e) {
            Log.e("network error", e.toString());
            return "network error";
        }
    }

    public String getMovieData(){
        StringBuilder builder = new StringBuilder();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setFollowRedirects(false);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                byte [] bytes = response.body().bytes();
                builder.append(new String(bytes, "GB2312"));
                return builder.toString();
            } else {
                return "server error";
            }
        } catch (IOException e) {
            Log.e("network error", e.toString());
            return "network error";
        }
    }

}
