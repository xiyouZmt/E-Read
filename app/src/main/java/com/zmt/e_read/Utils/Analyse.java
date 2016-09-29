package com.zmt.e_read.Utils;

import android.util.Log;

import com.zmt.e_read.Model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Dangelo on 2016/9/28.
 */
public class Analyse {

    public void analyseJsonData(boolean loading, String channelID, String jsonData, List<News> newsList){
        try {
            if(!loading){
                newsList.clear();
            }
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray(channelID.substring(0, channelID.length() - 1));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject newsObject = (JSONObject)jsonArray.get(i);
                String title = newsObject.getString("title");
                String time = newsObject.getString("ptime");
                String imageSrc = newsObject.getString("imgsrc");
                News.Builder builder = new News.Builder().setTitle(title).addImageUrl(imageSrc).setTime(time);
                if(newsObject.has("url_3w") || newsObject.has("url")){
                    builder.setType(News.TEXT_NEWS);
                    String content = newsObject.getString("digest");
                    String contentUrl;
                    if(newsObject.has("url_3w")){
                        contentUrl = newsObject.getString("url_3w");
                    } else {
                        contentUrl = newsObject.getString("url_3w");
                    }
                    builder.setDigest(content).setContentUrl(contentUrl);
                } else {
                    if(newsObject.has("imgextra")){
                        builder.setType(News.IMAGE_NEWS);
                        JSONArray imageArray = newsObject.getJSONArray("imgextra");
                        for (int j = 0; j <imageArray.length(); j++) {
                            JSONObject object = (JSONObject)imageArray.get(j);
                            builder.addImageUrl(object.get("imgsrc").toString());
                        }
                    }
                }
                News news = builder.build();
                newsList.add(news);
            }
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
    }

}
