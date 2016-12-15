package com.zmt.e_read.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zmt.e_read.Activity.AddChannel;
import com.zmt.e_read.Adapter.ChannelsAdapter;
import com.zmt.e_read.Module.ManageChannel;
import com.zmt.e_read.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    public static final String FILTER = "com.zmt.e_read.broadCast.adjustNewsFab";
    public static final String ALLCHANNELLIST = "allChannelList";

    private View view;
    private List<ManageChannel> userChannelList;
    private List<ManageChannel> allChannelList;
    private List<Fragment> newsFragmentList;
    private List<String> tabNameList;
    @BindView(R.id.channelTab)
    TabLayout channelTab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.addChannel)
    ImageView addChannel;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        initViews();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FILTER);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Animation animation;
                if (intent.getStringExtra("direction").equals("up")) {
                    if (fab.getVisibility() == View.VISIBLE) {
                        animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_out);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                fab.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        fab.startAnimation(animation);
                    }
                } else {
                    if (fab.getVisibility() == View.GONE) {
                        animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_in);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                fab.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        fab.startAnimation(animation);
                    }
                }
            }
        };
        manager.registerReceiver(receiver, intentFilter);
        return view;
    }

    @OnClick(R.id.addChannel)
    public void onClick() {
        Intent intent = new Intent(getActivity(), AddChannel.class);
        intent.putParcelableArrayListExtra(ALLCHANNELLIST, (ArrayList<? extends Parcelable>) allChannelList);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEvent(List<ManageChannel> allChannelList){
        if(allChannelList != null){
            this.allChannelList = allChannelList;
            reSetTabLayout(allChannelList);
        }
    }

    public void initViews() {
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        newsFragmentList = new ArrayList<>();
        tabNameList = new ArrayList<>();

        /**
         * 设置TabLayout模式
         */
        channelTab.setTabMode(TabLayout.MODE_SCROLLABLE);

        /**
         * 初始化channelData对象
         */
        String[] channelName = getResources().getStringArray(R.array.channelName);
        String[] channelID = getResources().getStringArray(R.array.channelID);
        userChannelList = new ArrayList<>();
        for (int i = 0; i < channelID.length; i++) {
            ManageChannel channelData = new ManageChannel();
            channelData.setId(channelID[i]).setName(channelName[i]).setType(ManageChannel.MYCHANNEL_VIEW);
            switch (channelData.getName()) {
                case ManageChannel.HEADLINE:
                    channelData.setStyle(ManageChannel.HEADLINE_TYPE);
                    break;
                case ManageChannel.HOUSE:
                    channelData.setStyle(ManageChannel.HOUSE_TYPE);
                    break;
                default:
                    channelData.setStyle(ManageChannel.OTHER_TYPE);
                    break;
            }
            userChannelList.add(channelData);
        }

        /**
         * 为TabLayout添加tab名称
         */
        for (int i = 0; i < userChannelList.size(); i++) {
            NewsListFragment newsListFragment = new NewsListFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ManageChannel.CHANNELID, userChannelList.get(i).getId());
            bundle.putString(ManageChannel.CHANNELNAME, userChannelList.get(i).getName());
            bundle.putString(ManageChannel.CHANNELSTYLE, userChannelList.get(i).getStyle());
            newsListFragment.setArguments(bundle);
            newsFragmentList.add(newsListFragment);
            channelTab.addTab(channelTab.newTab().setText(userChannelList.get(i).getName()));
        }

        /**
         * 加载viewPager的adapter
         */
        ChannelsAdapter adapter = new ChannelsAdapter(getChildFragmentManager(),
                Arrays.asList(channelName), newsFragmentList);
        /**
         * 为TabLayout设置viewPager
         */
        viewPager.setAdapter(adapter);
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(NewsListFragment.FILTER);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            }
        });
    }

    public void reSetTabLayout(List<ManageChannel> allChannelList){
        newsFragmentList.clear();
        tabNameList.clear();
        /**
         * 为TabLayout添加tab名称
         */
        for (int i = 1; i < allChannelList.size(); i++) {
            NewsListFragment newsListFragment = new NewsListFragment();
            ManageChannel channel = allChannelList.get(i);
            if(channel.getType().equals(ManageChannel.ALLCHANNEL_TEXT)){
                break;
            }
            Bundle bundle = new Bundle();
            bundle.putString(ManageChannel.CHANNELID, channel.getId());
            bundle.putString(ManageChannel.CHANNELNAME, channel.getName());
            bundle.putString(ManageChannel.CHANNELSTYLE, channel.getStyle());
            newsListFragment.setArguments(bundle);
            newsFragmentList.add(newsListFragment);
            tabNameList.add(channel.getName());
            channelTab.addTab(channelTab.newTab().setText(channel.getName()));
        }

        /**
         * 加载viewPager的adapter
         */
        ChannelsAdapter adapter = new ChannelsAdapter(getChildFragmentManager(),
                tabNameList, newsFragmentList);
        /**
         * 为TabLayout设置viewPager
         */
        viewPager.setAdapter(adapter);
        channelTab.setupWithViewPager(viewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
