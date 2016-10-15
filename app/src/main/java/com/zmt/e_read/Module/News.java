package com.zmt.e_read.Module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dangelo on 2016/9/27.
 */
public class News implements Serializable {

    public static final String TAG = "News";

    public static final String IMAGE_NEWS = "imageNews";

    public static final String TEXT_NEWS = "textNews";

    public static final String IMAGE_SRC = "imageSrc";

    public static final String IMAGE_COUNT = "imageCount";

    public static final String IMAGE_SIZE = "imageSize";

    private String type = "";
    private String title = "";
    private String digest = "";
    private String time = "";
    private String contentUrl = "";
    private String docId = "";
    private String source = "";

    private List<String> imageSrc = new ArrayList<>();

//    public News(Builder builder) {
//        this.type = builder.type;
//        this.title = builder.title;
//        this.digest = builder.digest;
//        this.time = builder.time;
//        this.contentUrl = builder.contentUrl;
//        this.imageSrc = builder.imageSrc;
//    }

    public News setSource(String source) {
        this.source = source;
        return this;
    }

    public News setDocId(String docId) {
        this.docId = docId;
        return this;
    }

    public News setType(String type) {
        this.type = type;
        return this;
    }

    public News setTitle(String title) {
        this.title = title;
        return this;
    }

    public News setDigest(String digest) {
        this.digest = digest;
        return this;
    }

    public News setTime(String time) {
        this.time = time;
        return this;
    }

    public News setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
        return this;
    }

    public News addImageSrc(String src) {
        imageSrc.add(src);
        return this;
    }

    public String getSource() {
        return source;
    }

    public String getDocId() {
        return docId;
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

//    public static class Builder{
//
//        private String type = "";
//        private String title = "";
//        private String digest = "";
//        private String time = "";
//        private String contentUrl = "";
//        private List<String> imageSrc = new ArrayList<>();
//
//        public Builder setType(String type){
//            this.type = type;
//            return this;
//        }
//
//        public Builder setTitle(String title) {
//            this.title = title;
//            return this;
//        }
//
//        public Builder setDigest(String digest) {
//            this.digest = digest;
//            return this;
//        }
//
//        public Builder setTime(String time) {
//            this.time = time;
//            return this;
//        }
//
//        public Builder addImageUrl(String imageSrc) {
//            this.imageSrc.add(imageSrc);
//            return this;
//        }
//
//        public Builder setContentUrl(String contentUrl) {
//            this.contentUrl = contentUrl;
//            return this;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public String getDigest() {
//            return digest;
//        }
//
//        public String getTime() {
//            return time;
//        }
//
//        public String getContentUrl() {
//            return contentUrl;
//        }
//
//        public List<String> getImageSrc() {
//            return imageSrc;
//        }
//
//        public News build(){
//            return new News(this);
//        }
//    }

}
