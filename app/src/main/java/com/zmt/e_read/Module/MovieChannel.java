package com.zmt.e_read.Module;

/**
 * Created by Dangelo on 2016/10/13.
 */
public class MovieChannel {

    public static final String NewestFilm = "dyzz/list_23_";

    public static final String channelName = "channelName";

    public static final String channelType = "channelType";

    private String type;
    private String name;

    public String getType() {
        return type;
    }

    public MovieChannel setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public MovieChannel setName(String name) {
        this.name = name;
        return this;
    }
}
