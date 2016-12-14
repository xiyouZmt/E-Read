package com.zmt.e_read.Module;

/**
 * Created by MintaoZhu on 2016/12/11.
 */
public class ManageChannel {

    public static final String MYCHANNEL_TEXT = "myChannelText";
    public static final String MYCHANNEL_VIEW = "myChannelView";
    public static final String ALLCHANNEL_TEXT = "allChannelText";
    public static final String ALLCHANNEL_VIEW = "allChannelView";

    private String id;

    private String name;

    private String style;

    private String type;

    private int tag = 0;

    public String getStyle() {
        return style;
    }

    public ManageChannel setStyle(String style) {
        this.style = style;
        return this;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ManageChannel setId(String id) {
        this.id = id;
        return this;
    }

    public ManageChannel setName(String name) {
        this.name = name;
        return this;
    }

    public ManageChannel setType(String type) {
        this.type = type;
        return this;
    }
}
