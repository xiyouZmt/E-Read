package com.zmt.e_read.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.zmt.e_read.Module.Video;
import com.zmt.e_read.R;
import com.zmt.e_read.Thread.DelayThread;
import com.zmt.e_read.Adapter.ViewHolder.ViewHolder;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MintaoZhu on 2016/11/23.
 */
public class VideoAdapter extends RecyclerView.Adapter {

    public  static final int GONE = 0x0001;
    public  static final int UPDATE = 0x002;

    private final int EMPTY_VIEW = 1;
    private final int PROGRESS_VIEW = 2;
    private Context context;
    private List<Video> videoList;
    private Handler handler;
    private Timer mTimer;
    private ViewHolder lastHolder;

    public  static int currentTime = 0;

    public  static boolean isShow = false;

    public VideoAdapter(Context context, List<Video> videoList, Handler handler) {
        this.context = context;
        this.videoList = videoList;
        this.handler = handler;
    }

    @Override
    public int getItemViewType(int position) {
        if(videoList.size() == 0){
            return EMPTY_VIEW;
        } else if(videoList.get(position) == null){
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ViewHolder){
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.videoView.setVideoURI(Uri.parse(videoList.get(position).getVideoUrl()));
            Glide.with(context).load(videoList.get(position).getVideoCover()).centerCrop().into(viewHolder.videoCover);
            viewHolder.videoTitle.setText(videoList.get(position).getVideoTitle());
            viewHolder.current_time.setText("00:00");
            viewHolder.seekBar.setProgress(0);
            viewHolder.seekBar.setMax(Integer.parseInt(videoList.get(position).getVideoLength()));
            final int videoLength = Integer.parseInt(videoList.get(position).getVideoLength());
            viewHolder.video_length.setText(secondToMinute(videoLength));
            if (!viewHolder.videoView.isPlaying()){
                viewHolder.power.setBackgroundResource(R.mipmap.pause);
            }

            viewHolder.power.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DelayThread delayThread;
                    /**
                     * 暂停
                     */
                    if (((ViewHolder) holder).videoView.isPlaying()) {
                        viewHolder.videoView.pause();
                        viewHolder.power.setBackgroundResource(R.mipmap.pause);
                        DelayThread.update = false;
                    } else {
                        if(lastHolder != null){
                            /**
                             * 继续播放
                             */
                            if(viewHolder == lastHolder){
                                viewHolder.videoView.start();
                                viewHolder.power.setBackgroundResource(R.mipmap.play);
                                /**
                                 * 更新进度条
                                 */
                                DelayThread.update = true;
                                delayThread = new DelayThread(viewHolder, handler, videoLength, currentTime);
                                delayThread.start();
                            }
                            /**
                             * 播放另一个(停止前一个)
                             */
                            else {
                                lastHolder.videoView.stopPlayback();
                                lastHolder.power.setBackgroundResource(R.mipmap.pause);
                                DelayThread.update = false;
                                lastHolder.current_time.setText("00:00");
                                lastHolder.seekBar.setProgress(0);

                                startPlay(viewHolder, videoLength);
                            }
                        } else {
                            viewHolder.videoCover.setVisibility(View.INVISIBLE);
                            startPlay(viewHolder, videoLength);
                        }
                    }
                    /**
                     * 功能键定时消失
                     */
                    if(mTimer != null){
                        mTimer.cancel();
                    }
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Message msg = handler.obtainMessage();
                            msg.what = GONE;
                            msg.obj = holder;
                            handler.sendMessage(msg);
                        }
                    }, 3000);
                    mTimer = timer;
                }
            });

            viewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    viewHolder.current_time.setText(secondToMinute(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    viewHolder.videoView.pause();
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int progress = seekBar.getProgress();
                    viewHolder.videoView.seekTo(progress * viewHolder.videoView.getDuration() / seekBar.getMax());
                    DelayThread delayThread = new DelayThread(viewHolder, handler, seekBar.getProgress());
                    delayThread.start();
                    viewHolder.videoView.start();
                }
            });

            viewHolder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    viewHolder.videoCover.setVisibility(View.VISIBLE);
                    viewHolder.power.setBackgroundResource(R.mipmap.pause);
                    viewHolder.current_time.setText("00:00");
                    viewHolder.seekBar.setProgress(0);
                    currentTime = 0;
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isShow) {
                        viewHolder.switchButton.setVisibility(View.GONE);
                        viewHolder.power.setVisibility(View.GONE);
                        viewHolder.videoTitle.setVisibility(View.GONE);
                        isShow = false;
                    } else {
                        viewHolder.switchButton.setVisibility(View.VISIBLE);
                        viewHolder.power.setVisibility(View.VISIBLE);
                        viewHolder.videoTitle.setVisibility(View.VISIBLE);
                        isShow = true;
                        if(mTimer != null){
                            mTimer.cancel();
                        }
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Message msg = handler.obtainMessage();
                                msg.what = GONE;
                                msg.obj = holder;
                                handler.sendMessage(msg);
                            }
                        }, 3000);
                        mTimer = timer;
                    }
                }
            });
        } else if(holder instanceof ProgressViewHolder){
            ProgressViewHolder viewHolder = (ProgressViewHolder) holder;
            viewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void startPlay(ViewHolder viewHolder, int videoLength){
        viewHolder.videoView.start();
        viewHolder.power.setBackgroundResource(R.mipmap.play);
        /**
         * 更新进度条
         */
        DelayThread.update = true;
        DelayThread delayThread = new DelayThread(viewHolder, handler, videoLength);
        delayThread.start();
        lastHolder = viewHolder;
    }

    public static String secondToMinute(int seconds){
        String minute;
        String second;
        if(seconds >= 60){
            int min = seconds / 60;
            int sec = seconds % 60;
            minute = min + "";
            second = sec + "";
            if(min < 10){
                minute = "0" + min;
            }
            if(sec < 10){
                second = "0" + sec;
            }
        } else if(seconds >= 10){
            minute = "00";
            second = seconds + "";
        } else {
            minute = "00";
            second = "0" + seconds;
        }
        return minute + ":" + second;
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.progressBar) ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
