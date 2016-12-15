package com.zmt.e_read.Fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.zmt.e_read.Activity.NewsDetailActivity;
import com.zmt.e_read.Activity.PhotoActivity;
import com.zmt.e_read.Adapter.AdapterInterface.OnItemClickListener;
import com.zmt.e_read.Adapter.NewsAdapter;
import com.zmt.e_read.Module.ManageChannel;
import com.zmt.e_read.Module.News;
import com.zmt.e_read.R;
import com.zmt.e_read.Thread.GetData;
import com.zmt.e_read.Utils.Analyse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListFragment extends android.support.v4.app.Fragment implements OnItemClickListener {

    private View view;
    private List<News> newsList;
    private String channelType = "";
    private String channelID = "";
    private String url = "";
    private NewsAdapter newsAdapter;
    private LinearLayoutManager manager;
    private boolean loading = false;
    private int start = 0;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    public static final String FILTER = "com.zmt.e_read.broadCast.newsToTop";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_list, container, false);
        initViews();
        /**
         * 添加数据到list中
         */
        Bundle bundle;
        if((bundle = getArguments()) != null){
            channelType = bundle.getString(ManageChannel.CHANNELSTYLE);
            channelID = bundle.getString(ManageChannel.CHANNELID);
        }
        progressBar.setVisibility(View.VISIBLE);
        url = ManageChannel.NEWS_DETAIL + channelType + channelID + start + ManageChannel.NEWS_COUNT;
        GetData getNewsList = new GetData(url, handler, News.TAG);
        Thread thread = new Thread(getNewsList, "GetData");
        thread.start();

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FILTER);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                recyclerView.smoothScrollToPosition(0);
            }
        };
        manager.registerReceiver(receiver, intentFilter);
        return view;
    }

    public final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Object object = msg.obj;
            if(object != null){
                switch (object.toString()){
                    case "network error" :
                        Snackbar.make(view, "网络连接错误!", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "server error" :
                        if(newsList.size() != 0){
                            newsAdapter.notifyItemRemoved(newsList.size() - 1);
                        }
                        Snackbar.make(view, "服务器连接错误!", Snackbar.LENGTH_SHORT).show();
                        break;
                    default :
                        Analyse analyse = new Analyse();
                        analyse.analyseNewsList(loading, channelID, object.toString(), newsList);
                        if(newsAdapter == null){
                            newsAdapter = new NewsAdapter(getContext(), newsList, NewsListFragment.this);
                            newsAdapter.addOnItemClickListener(NewsListFragment.this);
                        }
                        if(loading){
                            loading = false;
                            newsAdapter.notifyDataSetChanged();
                        } else {
                            recyclerView.setAdapter(newsAdapter);
                        }
                        break;
                }
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if(newsList.get(position).getType().equals(News.TEXT_NEWS)){
            intent.setClass(getActivity(), NewsDetailActivity.class);
            bundle.putSerializable(News.TEXT_NEWS, newsList.get(position));
        } else {
            intent.setClass(getActivity(), PhotoActivity.class);
            bundle.putSerializable(News.IMAGE_NEWS, newsList.get(position));
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public class ScrollListener extends RecyclerView.OnScrollListener {

        public ScrollListener() {
            super();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int itemCount = manager.getItemCount();
            int lastItemCount = manager.findLastVisibleItemPosition();
            if(itemCount <= (lastItemCount + 1)) {
                /**
                 * 加载更多
                 */
                loading = true;
                start += 20;
                String getMoreUrl = ManageChannel.NEWS_DETAIL + channelType + channelID + start + ManageChannel.NEWS_COUNT;
                GetData getNewsList = new GetData(getMoreUrl, handler, News.TAG);
                Thread thread = new Thread(getNewsList, "GetData");
                thread.start();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Intent intent = new Intent();
            intent.setAction(NewsFragment.FILTER);
            if(dy > 0){
                intent.putExtra("direction", "up");
            } else {
                intent.putExtra("direction", "down");
            }
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        }
    }

    private void initViews(){
        ButterKnife.bind(this, view);
        newsList = new ArrayList<>();

        /**
         * 获取fragment对应的频道ID
         */
        Object object;
        if((object = getArguments().get(ManageChannel.CHANNELID)) != null){
            channelID = object.toString();
        }

        /**
         * 初始化recyclerView
         */
        manager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
//        recyclerView.addItemDecoration(new SpaceItemDecoration(getContext(),
//                SpaceItemDecoration.VERTICAL_LIST));
        recyclerView.addOnScrollListener(new ScrollListener());

        /**
         * 初始化swipeRefreshLayout
         */
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_03a9f4));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loading = false;
                GetData getNewsList = new GetData(url, handler, News.TAG);
                Thread thread = new Thread(getNewsList, "GetData");
                thread.start();
            }
        });
    }
}
