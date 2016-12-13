package com.zmt.e_read.Adapter;

/**
 * Created by MintaoZhu on 2016/12/12.
 */
public interface ExchangeItem {
    void exchange(int startPosition, int endPosition);
    void deleteMyChannel(int myChannelPosition);
    void addToMyChannel(int allChannelPosition);
}
