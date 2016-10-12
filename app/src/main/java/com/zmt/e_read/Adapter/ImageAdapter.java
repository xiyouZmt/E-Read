package com.zmt.e_read.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.zmt.e_read.ImageLoader.ImageLoader;
import com.zmt.e_read.Model.Image;
import com.zmt.e_read.Model.OnItemClickListener;
import com.zmt.e_read.R;
import com.zmt.e_read.Utils.ProgressViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dangelo on 2016/10/11.
 */
public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Image> list;
    private OnItemClickListener clickListener;
    private final int EMPTY_VIEW = 1;
    private final int PROGRESS_VIEW = 2;

    public ImageAdapter(Context context, List<Image> list, OnItemClickListener clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if(list.size() == 0){
            return EMPTY_VIEW;
        } else if(list.get(position) == null){
            return PROGRESS_VIEW;
        }
        return super.getItemViewType(position);
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
            return new ViewHolder(view);
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
        if(holder instanceof ViewHolder){
            Picasso.with(context).load(list.get(position).getImageUrl()).into(((ViewHolder) holder).imageView);
//            Glide.with(context).load(list.get(position).getImageUrl()).centerCrop().into(((ViewHolder) holder).imageView);
//            ImageLoader.build(context).bindBitmap(list.get(position).getImageUrl(), ((ViewHolder) holder).imageView);
        } else if(holder instanceof ProgressViewHolder){
            ProgressViewHolder progressViewHolder = (ProgressViewHolder) holder;
            progressViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.imageView) ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
