package com.zmt.e_read.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmt.e_read.Adapter.ChannelsAdapter;
import com.zmt.e_read.Module.MovieChannel;
import com.zmt.e_read.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    View view;
    private List<MovieChannel> channelDataList;
    @BindView(R.id.channelTab) TabLayout channelTab;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.fab) FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_movie, container, false);
//        initViews();

        return view;
    }

//    public void initViews() {
//        ButterKnife.bind(this, view);
//        List<Fragment> movieFragmentList = new ArrayList<>();
//
//        /**
//         * 设置TabLayout模式
//         */
//        channelTab.setTabMode(TabLayout.MODE_FIXED);
//
//        /**
//         * 初始化channelData对象
//         */
//        String [] channelName = getResources().getStringArray(R.array.video_tab);
//        String [] channelType = getResources().getStringArray(R.array.video_type);
//        channelDataList = new ArrayList<>();
//        for (int i = 0; i < channelName.length; i++) {
//            MovieChannel channel = new MovieChannel();
//            channel.setName(channelName[i]).setType(channelType[i]);
//            channelDataList.add(channel);
//        }
//
//        /**
//         * 为TabLayout添加tab名称
//         */
//        for (int i = 0; i < channelName.length; i++) {
//            VideoListFragment videoListFragment = new VideoListFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString(MovieChannel.channelName, channelDataList.get(i).getName());
//            bundle.putString(MovieChannel.channelType, channelDataList.get(i).getType());
//            videoListFragment.setArguments(bundle);
//            movieFragmentList.add(videoListFragment);
//            channelTab.addTab(channelTab.newTab().setText(channelDataList.get(i).getName()));
//        }
//
//        /**
//         * 加载viewPager的adapter
//         */
//        ChannelsAdapter adapter = new ChannelsAdapter(getChildFragmentManager(),
//                Arrays.asList(channelName), movieFragmentList);
//        viewPager.setAdapter(adapter);
//        channelTab.setupWithViewPager(viewPager);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(MovieListFragment.FILTER);
//                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
//            }
//        });
//    }

}
