package com.zmt.e_read.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dangelo on 2016/9/27.
 */
public class News {

    public static final String IMAGE_NEWS = "imageNews";

    public static final String TEXT_NEWS = "textNews";

    private String type = "";
    private String title = "";
    private String digest = "";
    private String time = "";
    private String contentUrl = "";
    private List<String> imageSrc ;

    public News(Builder builder) {
        this.type = builder.type;
        this.title = builder.title;
        this.digest = builder.digest;
        this.time = builder.time;
        this.contentUrl = builder.contentUrl;
        this.imageSrc = builder.imageSrc;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public void setImageSrc(List<String> imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getType(){
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDigest() {
        return digest;
    }

    public String getTime() {
        return time;
    }

    public List<String> getImageUrl() {
        return imageSrc;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public static class Builder{

        private String type = "";
        private String title = "";
        private String digest = "";
        private String time = "";
        private String contentUrl = "";
        private List<String> imageSrc = new ArrayList<>();

        public Builder setType(String type){
            this.type = type;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDigest(String digest) {
            this.digest = digest;
            return this;
        }

        public Builder setTime(String time) {
            this.time = time;
            return this;
        }

        public Builder addImageUrl(String imageSrc) {
            this.imageSrc.add(imageSrc);
            return this;
        }

        public Builder setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
            return this;
        }

        public String getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        public String getDigest() {
            return digest;
        }

        public String getTime() {
            return time;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public List<String> getImageSrc() {
            return imageSrc;
        }

        public News build(){
            return new News(this);
        }
    }

}
