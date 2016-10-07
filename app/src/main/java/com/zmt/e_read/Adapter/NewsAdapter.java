package com.zmt.e_read.Adapter;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zmt.e_read.Model.ChannelData;
import com.zmt.e_read.Model.News;
import com.zmt.e_read.Model.OnItemClickListener;
import com.zmt.e_read.R;
import com.zmt.e_read.Utils.ProgressViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dangelo on 2016/9/27.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int EMPTY_VIEW = 1;
    private final int PROGRESS_VIEW = 2;
    private final int IMAGE_VIEW = 3;

    private Context context;
    private List<News> list;
    private OnItemClickListener clickListener;

    public NewsAdapter(Context context, List<News> list, OnItemClickListener clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }

    public void addOnItemClickListener(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if(list.size() == 0){
            return EMPTY_VIEW;
        } else if(list.get(position) == null){
            return PROGRESS_VIEW;
        } else if(list.get(position).getType().equals(News.IMAGE_NEWS)){
            return IMAGE_VIEW;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == PROGRESS_VIEW){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            return new ProgressViewHolder(view);
        } else if(viewType == EMPTY_VIEW){
            return null;
        } else if(viewType == IMAGE_VIEW){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
            return new ImageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
            return new NewsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(v, position);
            }
        });
        if(holder instanceof NewsViewHolder){
            NewsViewHolder viewHolder = (NewsViewHolder)holder;
            viewHolder.title.setText(list.get(position).getTitle());
            viewHolder.time.setText(list.get(position).getTime());
            /**
             * Glide加载图片
             */
            Glide.with(context).load(list.get(position).getImageUrl().get(0))
                    .override(dpToPx(72), dpToPx(72)).centerCrop().into(viewHolder.image);
            if(list.get(position).getType().equals(News.TEXT_NEWS)){
                viewHolder.digest.setText(list.get(position).getDigest());
            } else {
                viewHolder.digest.setText("");
            }
        } else if(holder instanceof ImageViewHolder){
            ImageViewHolder viewHolder = (ImageViewHolder)holder;
            viewHolder.title.setText(list.get(position).getTitle());
            viewHolder.time.setText(list.get(position).getTime());
            setItemImage(viewHolder, list, position);
        } else if(holder instanceof ProgressViewHolder){
            ProgressViewHolder viewHolder = (ProgressViewHolder)holder;
            viewHolder.progressBar.setIndeterminate(true);
        }
    }

    public void setItemImage(ImageViewHolder viewHolder, List<News> list, int position){
        viewHolder.imageMiddle.setVisibility(View.VISIBLE);
        viewHolder.imageRight.setVisibility(View.VISIBLE);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if(list.get(position).getImageUrl().size() == 1){
            Glide.with(context).load(list.get(position).getImageUrl().get(0))
                    .override(displayMetrics.widthPixels - dpToPx(10), dpToPx(90))
                    .centerCrop().into(viewHolder.imageLeft);
            viewHolder.imageMiddle.setVisibility(View.GONE);
            viewHolder.imageRight.setVisibility(View.GONE);
        } else if(list.get(position).getImageUrl().size() == 2){
            int imageWidth = (displayMetrics.widthPixels - dpToPx(20)) / 2;
            Glide.with(context).load(list.get(position).getImageUrl().get(0))
                    .override(imageWidth, dpToPx(90))
                    .centerCrop().into(viewHolder.imageLeft);
            Glide.with(context).load(list.get(position).getImageUrl().get(1))
                    .override(imageWidth, dpToPx(90))
                    .centerCrop().into(viewHolder.imageMiddle);
            viewHolder.imageRight.setVisibility(View.GONE);
        } else if(list.get(position).getImageUrl().size() >= 3){
            int imageWidth = (displayMetrics.widthPixels - dpToPx(30)) / 3;
            Glide.with(context).load(list.get(position).getImageUrl().get(0))
                    .override(imageWidth, dpToPx(90))
                    .centerCrop().into(viewHolder.imageLeft);
            Glide.with(context).load(list.get(position).getImageUrl().get(1))
                    .override(imageWidth, dpToPx(90))
                    .centerCrop().into(viewHolder.imageMiddle);
            Glide.with(context).load(list.get(position).getImageUrl().get(2))
                    .override(imageWidth, dpToPx(90))
                    .centerCrop().into(viewHolder.imageRight);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int dpToPx(float dp){
        float px = context.getResources().getDisplayMetrics().density;
        return (int)(dp * px + 0.5f);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.news_title)TextView title;
        @BindView(R.id.news_digest)TextView digest;
        @BindView(R.id.news_time)TextView time;
        @BindView(R.id.news_src)ImageView image;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.news_title) TextView title;
        @BindView(R.id.image_left) ImageView imageLeft;
        @BindView(R.id.image_right) ImageView imageRight;
        @BindView(R.id.image_middle) ImageView imageMiddle;
        @BindView(R.id.news_time) TextView time;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
