package com.zmt.e_read.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zmt.e_read.Model.News;
import com.zmt.e_read.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment {

    @BindView(R.id.photo_view) PhotoView photoView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    private View view;
    private String imageSrc = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_photo, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    public void initViews(){
        Bundle bundle;
        if((bundle = getArguments()) != null){
            imageSrc = bundle.getString(News.IMAGE_SRC);
//            imageCount = bundle.getInt(News.IMAGE_COUNT);
//            imageSize = bundle.getInt(News.IMAGE_SIZE);
        }
        Glide.with(this).load(imageSrc).asBitmap().format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.ic_load_fail).into(photoView);
        progressBar.setVisibility(View.GONE);
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {

            }

            @Override
            public void onOutsidePhotoTap() {

            }
        });
    }

}
