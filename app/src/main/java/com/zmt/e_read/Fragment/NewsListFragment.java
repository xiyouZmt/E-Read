package com.zmt.e_read.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmt.e_read.Adapter.NewsAdapter;
import com.zmt.e_read.Model.ChannelData;
import com.zmt.e_read.Model.News;
import com.zmt.e_read.Model.OnItemClickListener;
import com.zmt.e_read.R;
import com.zmt.e_read.Thread.GetNewsList;
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
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_list, container, false);
        initViews();
        /**
         * 添加数据到list中
         */
        Bundle bundle;
        if((bundle = getArguments()) != null){
            channelType = bundle.getString(ChannelData.channelType);
            channelID = bundle.getString(ChannelData.channelID);
        }
        url = ChannelData.NEWS_DETAIL + channelType + channelID + start + ChannelData.NEWS_COUNT;
        GetNewsList getNewsList = new GetNewsList(url, handler);
        Thread thread = new Thread(getNewsList, "GetNewsList");
        thread.start();
        return view;
    }

    public final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Object object = msg.obj;
            if(object != null){
                switch (object.toString()){
                    case "network error" :
                        break;
                    case "server error" :
                        break;
                    default :
                        Analyse analyse = new Analyse();
                        analyse.analyseJsonData(loading, channelID, object.toString(), newsList);
                        if(newsAdapter == null){
                            newsAdapter = new NewsAdapter(getContext(), newsList, NewsListFragment.this);
                            newsAdapter.addOnItemClickListener(NewsListFragment.this);
                        }
                        if(loading){
                            newsList.remove(null);
                            newsAdapter.notifyDataSetChanged();
                            loading = false;
                        } else {
                            recyclerView.setAdapter(newsAdapter);
                            if(swipeRefreshLayout.isRefreshing()){
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                        break;
                }
            }
        }
    };

    @Override
    public void onItemClick(View v, int position) {

    }

    public class ScrollListener extends RecyclerView.OnScrollListener {

        public ScrollListener() {
            super();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int itemCount = manager.getItemCount();
            int lastItemCount = manager.findLastVisibleItemPosition();
            if(!loading && itemCount <= (lastItemCount + 1)) {
                newsAdapter.notifyItemInserted(newsList.size() - 1);
                /**
                 * 加载更多
                 */
                newsList.add(null);
                loading = true;
                start += 20;
                String getMoreUrl = ChannelData.NEWS_DETAIL + channelType + channelID + start + ChannelData.NEWS_COUNT;
                GetNewsList getNewsList = new GetNewsList(getMoreUrl, handler);
                Thread thread = new Thread(getNewsList, "GetNewsList");
                thread.start();
//                newsAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initViews(){
        ButterKnife.bind(this, view);
        newsList = new ArrayList<>();

        /**
         * 获取fragment对应的频道ID
         */
        Object object;
        if((object = getArguments().get(ChannelData.channelID)) != null){
            channelID = object.toString();
        }

        /**
         * 初始化recyclerView
         */
        manager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false);
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
                GetNewsList getNewsList = new GetNewsList(url, handler);
                Thread thread = new Thread(getNewsList, "GetNewsList");
                thread.start();
            }
        });
    }

}
