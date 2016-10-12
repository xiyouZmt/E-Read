package com.zmt.e_read.Model;

/**
 * Created by Dangelo on 2016/10/11.
 */
public class Image {

    public static final String URL = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/";

    public static final int IMAGE_COUNT = 10;

    public static int NEXT_PAGE = 1;

    private String imageDesc;

    private String imageUrl;

    public String getImageDesc() {
        return imageDesc;
    }

    public Image setImageDesc(String imageDesc) {
        this.imageDesc = imageDesc;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Image setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
