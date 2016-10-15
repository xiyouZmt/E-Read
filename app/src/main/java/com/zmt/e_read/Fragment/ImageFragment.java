package com.zmt.e_read.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.zmt.e_read.Model.Image;
import com.zmt.e_read.Model.OnItemClickListener;
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
public class ImageFragment extends Fragment implements OnItemClickListener{

    private final int COLUMN = 2;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
//    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.gridView) GridView gridView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.fab) FloatingActionButton fab;
    private View view;
    private List<Image> imageList;
//    private MovieAdapter adapter;
    private GridAdapter adapter;
    private String url = Image.URL + Image.IMAGE_COUNT + "/" + Image.NEXT_PAGE;
    private boolean loading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_image, container, false);
        initViews();
        GetData getImageData = new GetData(url, handler, Image.TAG);
        Thread t = new Thread(getImageData, "getImageData");
        t.start();
        return view;
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Object object = msg.obj;
            if(object != null){
                switch (object.toString()){
                    case "network error" :
                        break;
                    case "server error" :
                        break;
                    default :
                        progressBar.setVisibility(View.GONE);
                        Analyse analyse = new Analyse();
                        analyse.analyseImage(loading, object.toString(), imageList);
                        if(adapter == null){
//                            adapter = new MovieAdapter(getActivity(), imageList, ImageFragment.this);
//                            recyclerView.setAdapter(adapter);
                            adapter = new GridAdapter(imageList, getContext());
                            gridView.setAdapter(adapter);
                        } else {
                            if(loading){
                                adapter.notifyDataSetChanged();
//                                adapter.notifyItemRemoved(manager.getItemCount());
                                loading = false;
                            } else {
                                swipeRefreshLayout.setRefreshing(false);
//                                adapter = new MovieAdapter(getContext(), imageList, ImageFragment.this);
//                                recyclerView.setAdapter(adapter);
                                adapter = new GridAdapter(imageList, getContext());
                                gridView.setAdapter(adapter);
                            }
                        }
                        break;
                }
            }
        }
    };

    public class ScrollListener implements AbsListView.OnScrollListener {

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
                String url = Image.URL + Image.IMAGE_COUNT + "/" + Image.NEXT_PAGE;
                GetData getImageData = new GetData(url, handler, Image.TAG);
                Thread t = new Thread(getImageData, "getImageData");
                t.start();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }

    public void initViews(){
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.color_array));
        progressBar.setVisibility(View.VISIBLE);
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
                url = Image.URL + Image.IMAGE_COUNT + "/" + Image.NEXT_PAGE;
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onItemClick(View v, int position) {

    }
}
