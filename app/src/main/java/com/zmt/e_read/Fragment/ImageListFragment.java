package com.zmt.e_read.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.zmt.e_read.Activity.PhotoActivity;
import com.zmt.e_read.Adapter.GridAdapter;
import com.zmt.e_read.Module.Image;
import com.zmt.e_read.Module.MovieChannel;
import com.zmt.e_read.R;
import com.zmt.e_read.Thread.GetData;
import com.zmt.e_read.Utils.AnalyseUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageListFragment extends Fragment{

    private final int COLUMN = 2;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
//    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private View view;
    private List<Image> imageList;
//    private MovieAdapter adapter;
    private GridAdapter adapter;
    private String url = "";
    private String [] image_tab ;
    private String channelName = "";
    private String channelType = "";
    private boolean loading = false;
    public static final String FILTER = "com.zmt.e_read.broadCast.imageToTop";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_image_list, container, false);
        initViews();
        url = channelType + Image.NEXT_PAGE;
        GetData getImageData = new GetData(url, handler, Image.TAG);
        Thread t = new Thread(getImageData, "getImageData");
        t.start();

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FILTER);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                gridView.setSelection(0);
            }
        };
        manager.registerReceiver(receiver, intentFilter);
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
                        if(imageList.size() != 0){
                            imageList.remove(imageList.size() - 1);
                            adapter.notifyDataSetChanged();
                        }
                        Snackbar.make(view, "服务器连接错误!", Snackbar.LENGTH_SHORT).show();
                        break;
                    default :
                        AnalyseUtils analyse = new AnalyseUtils();
                        if(channelName.equals(image_tab[0])){
                            analyse.analyseMeiZiImage(loading, object.toString(), imageList);
                        } else if(channelName.equals(image_tab[1])) {
                            analyse.analyseZhuangBiImage(loading, object.toString(), imageList);
                        }
                        if(adapter == null){
//                            adapter = new MovieAdapter(getActivity(), imageList, ImageListFragment.this);
//                            recyclerView.setAdapter(adapter);
                            adapter = new GridAdapter(imageList, getActivity());
                            gridView.setAdapter(adapter);
                        } else {
                            if(loading){
                                adapter.notifyDataSetChanged();
//                                adapter.notifyItemRemoved(manager.getItemCount());
                                loading = false;
                            } else {
//                                adapter = new MovieAdapter(getContext(), imageList, ImageListFragment.this);
//                                recyclerView.setAdapter(adapter);
                                adapter = new GridAdapter(imageList, getContext());
                                gridView.setAdapter(adapter);
                            }
                        }
                        break;
                }
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    public class ScrollListener implements AbsListView.OnScrollListener {

        int start = 0;
        public ScrollListener() {
            super();
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
//            LinearLayout.LayoutParams manager = (LinearLayout.LayoutParams) gridView.getLayoutParams();
//            int itemCount = manager.get();
//            int[] lastItemCount = manager.findLastVisibleItemPositions(null);
//            for (int aLastItemCount : lastItemCount) {
//                /**
//                 * 加载更多
//                 */
//                if ((aLastItemCount + COLUMN) >= itemCount) {
//                    imageList.add(null);
//                    adapter.notifyItemInserted(manager.getItemCount());
//                    adapter.notifyDataSetChanged();
//                    loading = true;
//                    Image.NEXT_PAGE++;
//                    String url = Image.URL + Image.IMAGE_COUNT + "/" + Image.NEXT_PAGE;
//                    GetData getImageData = new GetData(url, handler);
//                    Thread t = new Thread(getImageData, "getImageData");
//                    t.start();
//                    break;
//                }
//            }
            if(view.getLastVisiblePosition() == gridView.getCount() - 1){
                /**
                 * 加载更多
                 */
                imageList.add(null);
                adapter.notifyDataSetChanged();
                loading = true;
                Image.NEXT_PAGE++;
                String url = channelType + Image.NEXT_PAGE;
                GetData getImageData = new GetData(url, handler, Image.TAG);
                Thread t = new Thread(getImageData, "getImageData");
                t.start();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            Intent intent = new Intent();
            intent.setAction(ImageFragment.FILTER);
            if(firstVisibleItem > start){
                intent.putExtra("direction", "up");
                start = firstVisibleItem;
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            } else if(start > firstVisibleItem) {
                intent.putExtra("direction", "down");
                start = firstVisibleItem;
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            }
        }
    }

    public void initViews(){
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.color_array));
        progressBar.setVisibility(View.VISIBLE);
        image_tab = getResources().getStringArray(R.array.image_tab);
        /**
         * 获取fragment对应的频道ID
         */
        Bundle bundle;
        if((bundle = getArguments()) != null){
            channelName = bundle.getString(MovieChannel.channelName);
            channelType = bundle.getString(MovieChannel.channelType);
        }
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(COLUMN, StaggeredGridLayoutManager.VERTICAL));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addOnScrollListener(new ScrollListener());
        gridView.setOnScrollListener(new ScrollListener());
        imageList = new ArrayList<>();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loading = false;
                Image.NEXT_PAGE = 1;
                url = channelType + Image.NEXT_PAGE;
                GetData getNewsList = new GetData(url, handler, Image.TAG);
                Thread thread = new Thread(getNewsList, "GetData");
                thread.start();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Image.TAG, imageList.get(position));
                Intent intent = new Intent(getActivity(), PhotoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

}
