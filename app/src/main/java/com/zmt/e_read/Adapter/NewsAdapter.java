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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dangelo on 2016/9/27.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(list.get(position).getType().equals(News.TEXT_NEWS)){
            holder.title.setText(list.get(position).getTitle());
            holder.digest.setText(list.get(position).getDigest());
            holder.time.setText(list.get(position).getTime());
            Glide.with(context).load(list.get(position).getImageUrl().get(0))
                    .override(dpToPx(72), dpToPx(72)).centerCrop().into(holder.imgsrc);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(v, position);
            }
        });
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
