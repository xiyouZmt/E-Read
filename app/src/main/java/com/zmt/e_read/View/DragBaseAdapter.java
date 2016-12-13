package com.zmt.e_read.View;

/**
 * Created by MintaoZhu on 2016/11/4.
 */
public interface DragBaseAdapter {

    /**
     * 重新排列数据
     * @param oldPosition
     * @param newPosition
     */
    void exchangeItems(int oldPosition, int newPosition);


    /**
     * 设置某个item隐藏
     * @param hidePosition
     */
    void setHideItem(int hidePosition);

    /**
     * 删除某个item
     * @param removePosition
     */
    void removeItem(int removePosition);

    void setVisible(boolean visible);

}
