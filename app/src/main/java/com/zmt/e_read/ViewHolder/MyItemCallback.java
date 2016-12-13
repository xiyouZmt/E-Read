package com.zmt.e_read.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.zmt.e_read.Adapter.ManageChannelAdapter;

/**
 * Created by MintaoZhu on 2016/12/12.
 */
public class MyItemCallback extends ItemTouchHelper.Callback {

    private ManageChannelAdapter adapter;

    public MyItemCallback(ManageChannelAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * 是否拖住
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    /**
     * 是否删除
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = 0;
        int dragFlags = 0;
        if(viewHolder instanceof ManageChannelAdapter.MyChannelViewHolder){
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;
        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        adapter.exchange(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
