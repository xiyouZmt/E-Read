package com.zmt.e_read.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.zmt.e_read.Adapter.VideoAdapter;
import com.zmt.e_read.Module.MovieChannel;
import com.zmt.e_read.Module.Video;
import com.zmt.e_read.R;
import com.zmt.e_read.Thread.GetData;
import com.zmt.e_read.Utils.Analyse;
import com.zmt.e_read.ViewHolder.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoListFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private View view;
    private String url = "";
    private String channelID = "";
    private List<Video> videoList;
    private VideoAdapter adapter;
    private LinearLayoutManager manager;
    private boolean loading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_list, container, false);
        initViews();
        url = Video.url + channelID + Video.suffix;
        GetData getVideoList = new GetData(url, handler, Video.TAG);
        Thread thread = new Thread(getVideoList, "GetData");
        thread.start();

        return view;
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == VideoAdapter.GONE){
                ((ViewHolder) msg.obj).power.setVisibility(View.GONE);
                ((ViewHolder) msg.obj).switchButton.setVisibility(View.GONE);
                ((ViewHolder) msg.obj).videoTitle.setVisibility(View.GONE);
                VideoAdapter.isShow = false;
            } else if(msg.what == VideoAdapter.UPDATE){
                ((ViewHolder)msg.obj).seekBar.setProgress(msg.arg1);
                ((ViewHolder)msg.obj).current_time.setText(VideoAdapter.secondToMinute(msg.arg1));
            } else {
                Object object = msg.obj;
                if(object != null){
                    switch (object.toString()){
                        case "network error" :
                            Snackbar.make(view, "网络连接错误!", Snackbar.LENGTH_SHORT).show();
                            break;
                        case "server error" :
                            if(videoList.size() != 0){
                                if(videoList.get(videoList.size() - 1) == null){
                                    videoList.remove(videoList.size() - 1);
                                    adapter.notifyItemRemoved(videoList.size() - 1);
                                }
                            }
                            Snackbar.make(view, "服务器连接错误!", Snackbar.LENGTH_SHORT).show();
                            break;
                        default :
                            Analyse analyse = new Analyse();
                            analyse.analyseVideoList(loading, channelID, object.toString(), videoList);
                            if(adapter == null){
                                adapter = new VideoAdapter(getContext(), videoList, handler);
                            }
                            if(loading){
                                loading = false;
                                adapter.notifyDataSetChanged();
                            } else {
                                recyclerView.setAdapter(adapter);
                            }
                            break;
                    }
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }
    };

    public void initViews(){
        ButterKnife.bind(this, view);
        videoList = new ArrayList<>();
        Object object;
        if((object = getArguments().get(MovieChannel.channelType)) != null){
            channelID = object.toString();
        }
        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL ,false);
        recyclerView.setLayoutManager(manager);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetData getVideoList = new GetData(url, handler, Video.TAG);
                Thread thread = new Thread(getVideoList, "getVideoList");
                thread.start();
            }
        });
    }
}
