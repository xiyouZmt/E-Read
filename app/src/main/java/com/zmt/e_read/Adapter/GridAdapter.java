package com.zmt.e_read.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.zmt.e_read.Module.Image;
import com.zmt.e_read.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dangelo on 2016/10/12.
 */
public class GridAdapter extends BaseAdapter {

    private Context context;
    private List<Image> imageList;

    public GridAdapter(List<Image> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.image_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        if(imageList.get(position) != null) {
            Glide.with(context).load(imageList.get(position).getImageUrl()).into(viewHolder.imageView);
        }
        return convertView;
    }

    class ViewHolder{

        @BindView(R.id.imageView)
        ImageView imageView;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
