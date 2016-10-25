package com.zmt.e_read.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.zmt.e_read.Activity.MovieDetailActivity;
import com.zmt.e_read.Adapter.MovieAdapter;
import com.zmt.e_read.Module.Movie;
import com.zmt.e_read.Module.MovieChannel;
import com.zmt.e_read.Module.OnItemClickListener;
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
public class MovieListFragment extends Fragment implements OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private View view;
    private MovieAdapter adapter;
    private LinearLayoutManager manager;
    private List<Movie> movieList;
    private String channelType = "";
    private String url = "";
    private int currentPage = 1;
    private int pageCount;
    private boolean loading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_list, container, false);
        initViews();
        progressBar.setVisibility(View.VISIBLE);
        /**
         * get movie data
         */
        url = Movie.home_url + channelType + currentPage + Movie.url_suffix;
        GetData getNewsList = new GetData(url, handler, Movie.TAG);
        Thread thread = new Thread(getNewsList, "GetData");
        thread.start();
        return view;
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Object object = msg.obj;
            if(object != null){
                switch (object.toString()){
                    case "network error" :
                        Snackbar.make(view, "网络连接错误!", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "server error" :
                        Snackbar.make(view, "服务器连接错误!", Snackbar.LENGTH_SHORT).show();
                        break;
                    default :
                        Analyse analyse = new Analyse();
                        String movieCount = analyse.analyseMovieList(loading, channelType, Movie.GET, object.toString(), movieList);
                        pageCount = Integer.parseInt(movieCount.substring(movieCount.indexOf("共") + 1, movieCount.indexOf("页")));
                        if(adapter == null){
                            adapter = new MovieAdapter(movieList, MovieListFragment.this);
                        }
                        if(loading){
                            loading = false;
                            adapter.notifyDataSetChanged();
                        } else {
                            recyclerView.setAdapter(adapter);
                            if(progressBar.getVisibility() == View.VISIBLE){
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                        break;
                }
                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }
    };

    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Movie.TAG, movieList.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if(currentPage >= pageCount){
                /**
                 * all data have been loaded
                 */
                movieList.remove(movieList.size() - 1);
                adapter.notifyDataSetChanged();
                Snackbar.make(coordinatorLayout, "没有更多啦~", Snackbar.LENGTH_SHORT).show();
            } else {
                int itemCount = manager.getItemCount();
                int lastItemCount = manager.findLastVisibleItemPosition();
                if(itemCount <= (lastItemCount + 1)) {
                    /**
                     * reach the bottom and load more
                     */
                    loading = true;
                    currentPage ++;
                    String getMoreUrl = Movie.home_url + channelType + currentPage + Movie.url_suffix;
                    GetData getNewsList = new GetData(getMoreUrl, handler, Movie.TAG);
                    Thread thread = new Thread(getNewsList, "GetData");
                    thread.start();
                }
            }
        }
    }

    public void initViews(){
        ButterKnife.bind(this, view);
        movieList = new ArrayList<>();
        /**
         * 获取fragment对应的频道ID
         */
        Object object;
        if((object = getArguments().get(MovieChannel.channelType)) != null){
            channelType = object.toString();
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
                GetData getNewsList = new GetData(url, handler, Movie.TAG);
                Thread thread = new Thread(getNewsList, "GetData");
                thread.start();
            }
        });
    }

}
