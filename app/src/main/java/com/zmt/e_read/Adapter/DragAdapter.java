package com.zmt.e_read.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zmt.e_read.View.DragBaseAdapter;
import com.zmt.e_read.Module.ChannelData;
import com.zmt.e_read.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MintaoZhu on 2016/10/28.
 */
public class DragAdapter extends BaseAdapter implements DragBaseAdapter{

    private int holdPosition;
    private Context context;
    private List<ChannelData> channelDataList;
    private List<View> viewList;

    public DragAdapter(Context context, List<ChannelData> channelDataList) {
        this.context = context;
        this.channelDataList = channelDataList;
        viewList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return channelDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return channelDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.channel_item, parent, false);
        TextView textItem = (TextView)convertView.findViewById(R.id.text_item);
        textItem.setText(channelDataList.get(position).getName());
        if(position == holdPosition){
            convertView.setVisibility(View.INVISIBLE);
        }
        viewList.add(textItem);
//        if(position == 0 || position == 1){
//            viewHolder.textItem.setEnabled(false);
//        }
//        if(isChanged && (position == holdPosition) && !isItemShow){
//            viewHolder.textItem.setText("");
//            viewHolder.textItem.setSelected(true);
//            viewHolder.textItem.setEnabled(true);
//            isChanged = false;
//        }
//        if (!isVisible && (position == channelDataList.size() - 1)) {
//            viewHolder.textItem.setText("");
//            viewHolder.textItem.setSelected(true);
//            viewHolder.textItem.setEnabled(true);
//        }
//        if(remove_position == position){
//            viewHolder.textItem.setText("");
//        }
        return convertView;
    }

    public View getView(int position){
        return viewList.get(position);
    }

    @Override
    public void exchangeItems(int start, int end) {
//        ChannelData temp = channelDataList.get(oldPosition);
//        if(oldPosition < newPosition){
//            for(int i=oldPosition; i<newPosition; i++){
//                Collections.swap(channelDataList, i, i+1);
//            }
//        }else if(oldPosition > newPosition){
//            for(int i=oldPosition; i>newPosition; i--){
//                Collections.swap(channelDataList, i, i - 1);
//            }
//        }
//        channelDataList.set(newPosition, temp);

        holdPosition = end;
        ChannelData dragItem = (ChannelData)getItem(start);
        if (start < end) {
            channelDataList.add(end + 1, dragItem);
            channelDataList.remove(start);
        } else {
            channelDataList.add(end, dragItem);
            channelDataList.remove(start + 1);
        }
    }

    @Override
    public void setHideItem(int hidePosition) {
        holdPosition = hidePosition;
        notifyDataSetChanged();
    }

    @Override
    public void removeItem(int removePosition) {
        channelDataList.remove(removePosition);
        notifyDataSetChanged();
    }

    @Override
    public void setVisible(boolean visible) {

    }

//    class ViewHolder {
//        @BindView(R.id.text_item)
//        TextView textItem;
//        @BindView(R.id.icon_new)
//        ImageView iconNew;
//
//        ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }

//    public void setShowDropItem(boolean isItemShow){
//        this.isItemShow = isItemShow;
//    }
//
//    public void exchange(int start, int end) {
//        holdPosition = end;
//        ChannelData dragItem = (ChannelData)getItem(start);
//        if (start < end) {
//            channelDataList.add(end + 1, dragItem);
//            channelDataList.remove(start);
//        } else {
//            channelDataList.add(end, dragItem);
//            channelDataList.remove(start + 1);
//        }
//        isChanged = true;
//        notifyDataSetChanged();
//    }
//
//    public void addItem(ChannelData channel) {
//        channelDataList.add(channel);
//        notifyDataSetChanged();
//    }
//
//    public void remove(int position){
//        remove_position = position;
//        channelDataList.remove(position);
//        notifyDataSetChanged();
//    }
//
//    public boolean isVisible() {
//        return isVisible;
//    }
//
//    public void setVisible(boolean visible) {
//        isVisible = visible;
//    }
}
