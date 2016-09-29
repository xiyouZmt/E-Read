package com.zmt.e_read.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
        }
        return list.get(position) != null ? super.getItemViewType(position) : PROGRESS_VIEW;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == PROGRESS_VIEW){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            return new ProgressViewHolder(view);
        } else if(viewType == EMPTY_VIEW){
            return null;
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder)holder;
            viewHolder.title.setText(list.get(position).getTitle());
            viewHolder.time.setText(list.get(position).getTime());
            Glide.with(context).load(list.get(position).getImageUrl().get(0))
                    .override(dpToPx(72), dpToPx(72)).centerCrop().into(viewHolder.imgsrc);
            if(list.get(position).getType().equals(News.TEXT_NEWS)){
                viewHolder.digest.setText(list.get(position).getDigest());
            } else {
                viewHolder.digest.setText("");
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, position);
                }
            });
        } else if(holder instanceof ProgressViewHolder){
            ProgressViewHolder viewHolder = (ProgressViewHolder)holder;
            viewHolder.progressBar.setIndeterminate(true);
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

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.news_title)TextView title;
        @BindView(R.id.news_digest)TextView digest;
        @BindView(R.id.news_time)TextView time;
        @BindView(R.id.news_src)ImageView imgsrc;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
