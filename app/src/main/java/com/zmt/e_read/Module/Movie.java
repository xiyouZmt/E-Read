package com.zmt.e_read.Module;

import java.io.Serializable;

/**
 * Created by Dangelo on 2016/10/14.
 */
public class Movie implements Serializable {

    public static final String TAG = "movie";

    public static final String SEARCH = "search";

    public static final String GET = "get";

    public static final String url = "http://www.ygdy8.net";

    public static final String home_url = url + "/html/gndy/";

    public static final String url_suffix = ".html";

    public static final String search_url_prefix = "http://s.dydytt.net";

    public static final String search_url = search_url_prefix + "/plus/search.php?kwtype=0&searchtype=title&keyword=";

    public static final String search_home_url = "http://www.ygdy8.com";

    public static final String ERROR = "error";

    private String name;
    private String releaseTime;
    private String detailUrl;

    public String getName() {
        return name;
    }

    public Movie setName(String name) {
        this.name = name;
        return this;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public Movie setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
        return this;
    }

    public String getUrl() {
        return detailUrl;
    }

    public Movie setUrl(String url) {
        this.detailUrl = url;
        return this;
    }
}
