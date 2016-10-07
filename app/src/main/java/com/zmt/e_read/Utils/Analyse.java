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

    public void analyseNewsList(boolean loading, String channelID, String jsonData, List<News> newsList){
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
                News news = new News();
                news.setTitle(title).addImageSrc(imageSrc).setTime(time);
                if(newsObject.has("url_3w") || newsObject.has("url")){
                    news.setType(News.TEXT_NEWS);
                    String content = newsObject.getString("digest");
                    String docId = newsObject.getString("docid");
                    String source = newsObject.getString("source");
                    String contentUrl;
                    if(newsObject.has("url_3w")){
                        contentUrl = newsObject.getString("url_3w");
                    } else {
                        contentUrl = newsObject.getString("url_3w");
                    }
                    news.setSource(source).setDigest(content).setDocId(docId).setContentUrl(contentUrl);
                } else {
                    news.setType(News.IMAGE_NEWS);
                    if(newsObject.has("imgextra")){
                        JSONArray imageArray = newsObject.getJSONArray("imgextra");
                        for (int j = 0; j <imageArray.length(); j++) {
                            JSONObject object = (JSONObject)imageArray.get(j);
                            news.addImageSrc(object.get("imgsrc").toString());
                        }
                    }
                }
                newsList.add(news);
            }
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
    }

    public String analyseNewsDetail(String docId, String jsonData){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject object = (JSONObject) jsonObject.get(docId);
            String news_detail = object.get("body").toString();
            String imageUrl;
            String imageElement;
            JSONArray jsonArray = object.getJSONArray("img");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject imageObject = (JSONObject) jsonArray.get(i);
                imageUrl = imageObject.get("src").toString();
                imageElement = "<img src=\"" + imageUrl +  "\"/><br>";
                news_detail = news_detail.replace(imageObject.get("ref").toString(), imageElement);
            }
            return news_detail;
        } catch (JSONException e) {
            Log.e("json error", e.toString());
            return null;
        }
    }
}
