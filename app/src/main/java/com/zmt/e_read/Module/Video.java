package com.zmt.e_read.Module;

/**
 * Created by MintaoZhu on 2016/11/22.
 */
public class Video {

    public static final String TAG = "video";

    public static final String url = "http://c.3g.163.com/nc/video/list/";

    public static final String suffix = "/n/10-10.html";

    private String videoUrl;
    private String videoTitle;
    private String videoLength;
    private String videoCover;

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public String getVideoLength() {
        return videoLength;
    }

    public Video setVideoLength(String videoLength) {
        this.videoLength = videoLength;
        return this;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public Video setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public Video setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }
}
