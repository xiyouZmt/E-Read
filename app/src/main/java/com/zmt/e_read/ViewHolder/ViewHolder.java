package com.zmt.e_read.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.zmt.e_read.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MintaoZhu on 2016/11/25.
 */
public class ViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.videoView) public VideoView videoView;
    @BindView(R.id.videoCover) public ImageView videoCover;
    @BindView(R.id.video_title) public TextView videoTitle;
    @BindView(R.id.power) public ImageView power;
    @BindView(R.id.switchButton) public LinearLayout switchButton;
    @BindView(R.id.current_time) public TextView current_time;
    @BindView(R.id.video_length) public TextView video_length;
    @BindView(R.id.seekBar) public SeekBar seekBar;
    @BindView(R.id.full_screen) public LinearLayout full_screen;

    public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
