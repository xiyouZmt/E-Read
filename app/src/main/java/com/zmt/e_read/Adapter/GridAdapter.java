package com.zmt.e_read.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.zmt.e_read.GridImageView;
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
        Fresco.initialize(context);
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
//            if(imageList.get(position) != null && viewHolder != null){
//                Glide.with(context).load(imageList.get(position).getImageUrl()).centerCrop().into(viewHolder.imageView);
//            } else {
//                convertView = LayoutInflater.from(context).inflate(R.layout.progressbar_item, null);
//            }
        }
        if(imageList.get(position) != null) {
//            Picasso.with(context).load(imageList.get(position).getImageUrl()).into(viewHolder.imageView);
//            Glide.with(context).load(imageList.get(position).getImageUrl()).centerCrop().into(viewHolder.imageView);
//            ImageLoader.build(context).bindBitmap(imageList.get(position).getImageUrl(), (viewHolder.imageView));
            if(imageList.get(position).getImageDesc().endsWith(Image.GIF)){
                DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                        .setAutoPlayAnimations(true).setUri(Uri.parse(imageList.get(position).getImageUrl())).build();
                viewHolder.imageView.setController(draweeController);
            } else {
                viewHolder.imageView.setImageURI(Uri.parse(imageList.get(position).getImageUrl()));
            }
        }
        return convertView;
    }

    class ViewHolder{

        @BindView(R.id.imageView)
        SimpleDraweeView imageView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            imageView.setAspectRatio(1f);
        }
    }
}
