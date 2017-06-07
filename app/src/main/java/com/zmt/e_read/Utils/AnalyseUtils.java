package com.zmt.e_read.Utils;

import android.util.Log;

import com.zmt.e_read.Module.Image;
import com.zmt.e_read.Module.Movie;
import com.zmt.e_read.Module.MovieChannel;
import com.zmt.e_read.Module.News;
import com.zmt.e_read.Module.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dangelo on 2016/9/28.
 */
public class AnalyseUtils {

    public void analyseNewsList(boolean loading, String channelID, String jsonData, List<News> newsList){
        try {
            if(!loading){
                newsList.clear();
            } else if(newsList.size() != 0 && newsList.get(newsList.size() - 1) == null){
                newsList.remove(newsList.size() - 1);
            }
            JSONObject jsonObject = new JSONObject(jsonData);
            if(channelID.equals("5YyX5Lqs/")){  //房产对应的json数组的键为北京
                channelID = "北京";
            } else {
                channelID = channelID.substring(0, channelID.length() - 1);
            }
            JSONArray jsonArray = jsonObject.getJSONArray(channelID);
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
                        contentUrl = newsObject.getString("url");
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
            newsList.add(null);
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

//    public void analyseZhuangBiImage(boolean loading, String html, List<Image> imageList){
//        if(!loading){
//            imageList.clear();
//        } else if(imageList.size() != 0) {
//            imageList.remove(imageList.size() - 1);
//        }
//        Document document = Jsoup.parse(html);
//        Elements elements = document.getElementsByClass("picture-list");
//        for (Element element : elements) {
//            if (element.tagName().equals("div")){
//                for (Element ele : element.getAllElements()) {
//                    if(ele.tagName().equals("a") && !ele.attr("href").equals("")){
//                        Image image = new Image();
//                        image.setImageUrl(ele.attr("href"));
//                        if(image.getImageUrl().endsWith(Image.GIF)){
//                            image.setImageDesc(Image.GIF);
//                        } else {
//                            image.setImageDesc(Image.JPG);
//                        }
//                        imageList.add(image);
//                    }
//                }
//            }
//        }
//    }

    public void analyseMeiZiImage(boolean loading, String jsonData, List<Image> imageList){
        if(!loading){
            imageList.clear();
        } else if(imageList.size() != 0){
            imageList.remove(imageList.size() - 1);
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                Image image = new Image();
                image.setImageDesc(object.get("desc").toString())
                        .setImageUrl(object.get("url").toString());
                imageList.add(image);
            }
        } catch (JSONException e) {
            Log.e("json error", e.toString());
        }
    }

    public String analyseMovieList(boolean loading, String movieType, String style, String html, List<Movie> movieList){
        if(!loading){
            movieList.clear();
        } else if(movieList.size() != 0){
            movieList.remove(movieList.size() - 1);
        }
        int count = 1;
        if(movieType.equals(MovieChannel.NewestFilm)){
            count = 0;
        }
        try{
            Document document = Jsoup.parse(html);
            Elements elements = document.getElementsByClass("co_content8");
            for (Element element : elements) {
                if(element.tagName().equals("div")){
                    /**
                     * movie node
                     */
                    Elements movieElements = element.getElementsByTag("table");
                    for (Element movieElement : movieElements){
                        /**
                         * Link Node，include movie link and movie name
                         */
                        Elements hrefElement = movieElement.getElementsByTag("a");
                        Log.e("link node", hrefElement.size() + "");
                        String movieName = hrefElement.get(count).text();
                        String movieUrlSuffix = hrefElement.get(count).attr("href");
                        Elements timeElement = movieElement.getElementsByTag("font");
                        String releaseTime = timeElement.last().text();
                        Movie movie = new Movie();
                        movie.setName(movieName).setReleaseTime(releaseTime);
                        if(style.equals(Movie.GET)){
                            movie.setUrl(Movie.url + movieUrlSuffix);
                        } else if(style.equals(Movie.SEARCH)){
                            movie.setUrl(Movie.search_home_url + movieUrlSuffix);
                        }
                        movieList.add(movie);
                    }
                    if(style.equals(Movie.GET)){
                        movieList.add(null);
                        /**
                         * movie count
                         */
                        Elements countElements = element.getElementsByClass("x");
                        return countElements.get(0).text();
                    } else {
                        return "success";
                    }
                }
            }
            return Movie.ERROR;
        } catch(Exception e){
            Log.e("analyse error", e.toString());
            return Movie.TAG;
        }
    }

    public void analyseMovieDetail(String html, Map<String, String> map){
        int count = 0;
        Document document = Jsoup.parse(html);
        Log.e("html", document.toString());
        Element element = document.getElementById("Zoom");
        Elements elements = element.getElementsByTag("p");

        Elements downloadElement = element.getElementsByTag("tbody");
        String downloadUrl = downloadElement.get(count).getElementsByTag("a").get(count).attr("href");

        Elements imageElements = element.getElementsByTag("img");
        
        map.put("movie_downloadUrl", downloadUrl);
        map.put("movie_image", imageElements.get(count).attr("src"));
        map.put("movie_content", elements.text());
        if(imageElements.size() == 2){
            map.put("movie_preview", imageElements.get(1).attr("src"));
        }
    }

    public void analyseVideoList(boolean loading, String channelID, String jsonData, List<Video> videoList){
        try {
            if(!loading){
                videoList.clear();
            } else if(videoList.size() != 0){
                videoList.remove(videoList.size() - 1);
            }
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray(channelID);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject videoObject = (JSONObject)jsonArray.get(i);
                String title = videoObject.getString("title");
                String length = videoObject.getString("length");
                String cover = videoObject.getString("cover");
                String videoUrl;
                if(videoObject.has("mp4Hd_url")){
                    videoUrl = videoObject.getString("mp4Hd_url");
                } else if(videoObject.has("mp4_url")){
                    videoUrl = videoObject.getString("mp4Hd_url");
                } else {
                    continue;
                }
                Video video = new Video();
                video.setVideoTitle(title).setVideoLength(length).setVideoUrl(videoUrl).setVideoCover(cover);
                videoList.add(video);
            }
            videoList.add(null);
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
    }

}