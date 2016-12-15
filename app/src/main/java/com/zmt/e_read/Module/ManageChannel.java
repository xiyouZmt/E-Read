package com.zmt.e_read.Module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MintaoZhu on 2016/12/11.
 */
public class ManageChannel implements Parcelable {

    public static final String NETEASE_HOST = "http://c.m.163.com/";

    public static final String NEWS_DETAIL = NETEASE_HOST + "nc/article/";

    public static final String HEADLINE_TYPE = "headline/";

    public static final String HOUSE_TYPE = "house/";

    public static final String OTHER_TYPE = "list/";

    public static final String NEWS_COUNT = "-20.html";

    public static final String END_DETAIL = "/full.html";

    public static final String HEADLINE = "头条";

    public static final String HOUSE = "房产";

    public static final String CHANNELID = "CHANNEL_ID";

    public static final String CHANNELNAME = "CHANNEL_NAME";

    public static final String CHANNELSTYLE = "CHANNEL_STYLE";

    public static final String MYCHANNEL_TEXT = "myChannelText";
    public static final String MYCHANNEL_VIEW = "myChannelView";
    public static final String ALLCHANNEL_TEXT = "allChannelText";
    public static final String ALLCHANNEL_VIEW = "allChannelView";

    private String id;

    private String name;

    private String style;

    private String type;

    private int tag = 0;

    public ManageChannel() {}

    protected ManageChannel(Parcel in) {
        id = in.readString();
        name = in.readString();
        style = in.readString();
        type = in.readString();
        tag = in.readInt();
    }

    public static final Creator<ManageChannel> CREATOR = new Creator<ManageChannel>() {
        @Override
        public ManageChannel createFromParcel(Parcel in) {
            return new ManageChannel(in);
        }

        @Override
        public ManageChannel[] newArray(int size) {
            return new ManageChannel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(style);
        dest.writeString(type);
        dest.writeInt(tag);
    }
}
