package com.zmt.e_read.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmt.e_read.Module.ManageChannel;
import com.zmt.e_read.R;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MintaoZhu on 2016/12/11.
 */
public class ManageChannelAdapter extends RecyclerView.Adapter implements ExchangeItem {

    private final int MY_CHANNEL_VIEW = 0x001;
    private final int ALL_CHANNEL_TEXT = 0x002;
    private final int ALL_CHANNEL_VIEW = 0x003;
    private final int COMPLETE = 0;
    private final int EDIT_ICON = 1;
    private List<ManageChannel> channelDataList;
    private boolean edit = false;

    public ManageChannelAdapter(List<ManageChannel> channelDataList) {
        this.channelDataList = channelDataList;
    }

    @Override
    public int getItemViewType(int position) {
        String type = channelDataList.get(position).getType();
        int channelType ;
        switch (type){
            case ManageChannel.MYCHANNEL_VIEW :
                channelType = MY_CHANNEL_VIEW;
                return channelType;
            case ManageChannel.ALLCHANNEL_TEXT :
                channelType = ALL_CHANNEL_TEXT;
                return channelType;
            case ManageChannel.ALLCHANNEL_VIEW :
                channelType = ALL_CHANNEL_VIEW;
                return channelType;
            default:
                return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == MY_CHANNEL_VIEW){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_channel_view, parent, false);
            return new MyChannelViewHolder(view);
        } else if(viewType == ALL_CHANNEL_TEXT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_channel_text, parent, false);
            return new AllChannelTextViewHolder(view);
        } else if(viewType == ALL_CHANNEL_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_channel_view, parent, false);
            return new AllChannelViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_channel_text, parent, false);
            return new MyChannelTextViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyChannelTextViewHolder){
            final MyChannelTextViewHolder viewHolder = (MyChannelTextViewHolder) holder;
            viewHolder.manageChannel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(viewHolder.dragText.getVisibility() == View.GONE){
                        viewHolder.dragText.setVisibility(View.VISIBLE);
                        viewHolder.manageChannel.setText("完成");
                        edit = true;
                        /**
                         * 显示删除图标
                         */
                        for (int i = 0; i < channelDataList.size(); i++) {
                            ManageChannel channel = channelDataList.get(i);
                            if(channel.getType().equals(ManageChannel.MYCHANNEL_VIEW)){
                                channel.setTag(EDIT_ICON);
                            }
                        }
                        notifyDataSetChanged();
                    } else {
                        viewHolder.dragText.setVisibility(View.GONE);
                        viewHolder.manageChannel.setText("编辑");
                        edit = false;
                        /**
                         * 隐藏删除图标
                         */
                        for (int i = 0; i < channelDataList.size(); i++) {
                            ManageChannel channel = channelDataList.get(i);
                            if(channel.getType().equals(ManageChannel.MYCHANNEL_VIEW)){
                                channel.setTag(COMPLETE);
                            }
                        }
                        notifyDataSetChanged();
                    }
                }
            });
        } else if(holder instanceof MyChannelViewHolder) {
            final MyChannelViewHolder viewHolder = (MyChannelViewHolder) holder;
            viewHolder.myChannel.setText(channelDataList.get(position).getName());
            if(channelDataList.get(position).getTag() == EDIT_ICON){
                viewHolder.deleteChannel.setVisibility(View.VISIBLE);
            } else {
                viewHolder.deleteChannel.setVisibility(View.GONE);
            }
            viewHolder.myChannel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(viewHolder.deleteChannel.getVisibility() == View.VISIBLE){
                        deleteMyChannel(position);
                    } else {

                    }
                }
            });
        } else if(holder instanceof AllChannelTextViewHolder){

        } else if(holder instanceof AllChannelViewHolder){
            AllChannelViewHolder viewHolder = (AllChannelViewHolder) holder;
            viewHolder.allChannel.setText(channelDataList.get(position).getName());
            viewHolder.allChannel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToMyChannel(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return channelDataList.size();
    }

    @Override
    public void exchange(int startPosition, int endPosition) {
        ManageChannel startChannel = channelDataList.get(startPosition);
        ManageChannel endChannel = channelDataList.get(endPosition);
        if(startChannel.getType().equals(ManageChannel.MYCHANNEL_VIEW)
            && endChannel.getType().equals(ManageChannel.MYCHANNEL_VIEW)){
            Collections.swap(channelDataList, startPosition, endPosition);
            notifyItemMoved(startPosition, endPosition);
        }
    }

    @Override
    public void deleteMyChannel(int myChannelPosition) {
        ManageChannel channel = channelDataList.get(myChannelPosition);
        int addPosition = 0;
        for (int i = 0; i < channelDataList.size(); i++) {
            if(channelDataList.get(i).getType().equals(ManageChannel.ALLCHANNEL_VIEW)){
                addPosition = i;
                break;
            }
        }
        /**
         * 从我的频道删除
         */
        channelDataList.remove(myChannelPosition);
        notifyItemRemoved(myChannelPosition);

        /**
         * 添加到所有频道
         */
        channel.setType(ManageChannel.ALLCHANNEL_VIEW);
        channelDataList.add(addPosition - 1, channel);
        notifyItemInserted(addPosition - 1);

    }

    @Override
    public void addToMyChannel(int allChannelPosition) {
        ManageChannel channel = channelDataList.get(allChannelPosition);
        int addPosition = 0;
        for (int i = 0; i < channelDataList.size(); i++) {
            if(channelDataList.get(i).getType().equals(ManageChannel.ALLCHANNEL_TEXT)){
                addPosition = i;
                break;
            }
        }
        /**
         * 从所有频道中删除
         */
        channelDataList.remove(allChannelPosition);
        notifyItemRemoved(allChannelPosition);
        /**
         * 添加到我的频道
         */
        channel.setType(ManageChannel.MYCHANNEL_VIEW);
        if(edit){
            channel.setTag(EDIT_ICON);
        }
        channelDataList.add(addPosition, channel);
        notifyItemInserted(addPosition);
    }

    public class MyChannelTextViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.drag_text)
        TextView dragText;
        @BindView(R.id.manageChannel)
        TextView manageChannel;
        public MyChannelTextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class MyChannelViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.myChannel) TextView myChannel;
        @BindView(R.id.delete) ImageView deleteChannel;

        public MyChannelViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class AllChannelTextViewHolder extends RecyclerView.ViewHolder{

        public AllChannelTextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class AllChannelViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.allChannel) TextView allChannel;
        public AllChannelViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
