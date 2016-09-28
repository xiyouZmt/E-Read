package com.zmt.e_read.Fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zmt.e_read.Adapter.ChannelsAdapter;
import com.zmt.e_read.R;
import com.zmt.e_read.Model.ChannelData;

import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    private View view;
    private List<ChannelData> channelDataList;
    @BindView(R.id.channelTab) TabLayout channelTab;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.addChannel) ImageView addChannel;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_news, container, false);
        initViews();

        return view;
    }

    public void initViews(){
        ButterKnife.bind(this, view);
        List<Fragment> newsFragmentList = new ArrayList<>();

        /**
         * 设置TabLayout模式
         */
        channelTab.setTabMode(TabLayout.MODE_FIXED);

        /**
         * 初始化channelData对象
         */
        String [] channelName = getResources().getStringArray(R.array.channelName);
        String [] channelID = getResources().getStringArray(R.array.channelID);
        channelDataList = new ArrayList<>();
        for (int i = 0; i < channelID.length; i++) {
            ChannelData channelData;
            ChannelData.Builder builder = new ChannelData.Builder().setId(channelID[i]).setName(channelName[i]);
            switch (builder.getName()){
                case ChannelData.HEADLINE :
                    channelData = builder.setType(ChannelData.HEADLINE_TYPE).build();
                    break;
                case ChannelData.HOUSE :
                    channelData = builder.setType(ChannelData.HOUSE_TYPE).build();
                    break;
                default:
                    channelData = builder.setType(ChannelData.OTHER_TYPE).build();
                    break;
            }
            channelDataList.add(channelData);
        }

        /**
         * 为TabLayout添加tab名称
         */
        for (int i = 0; i < channelID.length; i++) {
            NewsListFragment newsListFragment = new NewsListFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ChannelData.channelID, channelDataList.get(i).getId());
            bundle.putString(ChannelData.channelName, channelDataList.get(i).getName());
            bundle.putString(ChannelData.channelType, channelDataList.get(i).getType());
            newsListFragment.setArguments(bundle);
            newsFragmentList.add(newsListFragment);
            channelTab.addTab(channelTab.newTab().setText(channelDataList.get(i).getName()));
        }

        /**
         * 加载viewPager的adapter
         */
        ChannelsAdapter adapter = new ChannelsAdapter(getChildFragmentManager(),
                Arrays.asList(channelName), newsFragmentList);
        viewPager.setAdapter(adapter);

        /**
         * 为TabLayout加载viewPager
         */
        channelTab.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
