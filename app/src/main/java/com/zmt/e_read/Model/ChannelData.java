package com.zmt.e_read.Model;

/**
 * Created by Dangelo on 2016/9/24.
 */
public class ChannelData {

    public static final String NETEASE_HOST = "http://c.m.163.com/";

    public static final String NEWS_DETAIL = NETEASE_HOST + "nc/article/";

    public static final String HEADLINE_TYPE = "headline/";

    public static final String HOUSE_TYPE = "house/";

    public static final String OTHER_TYPE = "list/";

    public static final String NEWS_COUNT = "-20.html";

    public static final String END_DETAIL = "/full.html";

    public static final String HEADLINE = "头条";

    public static final String HOUSE = "房产";

    public static final String channelID = "CHANNEL_ID";

    public static final String channelName = "CHANNEL_NAME";

    public static final String channelType = "CHANNEL_TYPE";

    private String id;

    private String name;

    private String type;

//    public ChannelData(Builder builder) {
//        this.id = builder.id;
//        this.name = builder.name;
//        this.type = builder.type;
//    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ChannelData setId(String id) {
        this.id = id;
        return this;
    }

    public ChannelData setName(String name) {
        this.name = name;
        return this;
    }

    public ChannelData setType(String type) {
        this.type = type;
        return this;
    }

//    public static class Builder{
//
//        private String id;
//
//        private String name;
//
//        private String type;
//
//        public String getId() {
//            return id;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public Builder setType(String type) {
//            this.type = type;
//            return this;
//        }
//
//        public Builder setId(String id) {
//            this.id = id;
//            return this;
//        }
//
//        public Builder setName(String name) {
//            this.name = name;
//            return this;
//        }
//
//        public ChannelData build(){
//            return new ChannelData(this);
//        }
//    }
}
