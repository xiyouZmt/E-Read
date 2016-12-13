package com.zmt.e_read.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.zmt.e_read.Adapter.ChannelsAdapter;
import com.zmt.e_read.Adapter.DragAdapter;
import com.zmt.e_read.Adapter.ManageChannelAdapter;
import com.zmt.e_read.Module.ChannelData;
import com.zmt.e_read.Module.ManageChannel;
import com.zmt.e_read.R;
import com.zmt.e_read.Utils.SpaceItemDecoration;
import com.zmt.e_read.ViewHolder.MyItemCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddChannel extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.channelRecycler)
    RecyclerView channelRecycler;
    private List<ManageChannel> allChannelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_channel);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        switch (parent.getId()){
            case R.id.myChannel :

                break;
            case R.id.allChannel :

                break;
        }
    }

    public void initViews() {
        ButterKnife.bind(this);
        toolbar.setTitle("频道管理");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        allChannelList = new ArrayList<>();
        /**
         * header-part
         */
        ManageChannel myChannel = new ManageChannel();
        myChannel.setName("").setId("").setType(ManageChannel.MYCHANNEL_TEXT);
        allChannelList.add(myChannel);

        /**
         * user channel data
         */
        String[] myChannelName = getResources().getStringArray(R.array.channelName);
        String[] myChannelID = getResources().getStringArray(R.array.channelID);
        for (int i = 0; i < myChannelName.length; i++) {
            ManageChannel myChannelData = new ManageChannel();
            myChannelData.setName(myChannelName[i]).setId(myChannelID[i]).setType(ManageChannel.MYCHANNEL_VIEW);
            allChannelList.add(myChannelData);
        }

        /**
         * middle
         */
        ManageChannel allChannel = new ManageChannel();
        allChannel.setName("").setId("").setType(ManageChannel.ALLCHANNEL_TEXT);
        allChannelList.add(allChannel);

        /**
         * all channel data
         */
        String[] allChannelName = getResources().getStringArray(R.array.allChannelName);
        String[] allChannelID = getResources().getStringArray(R.array.allChannelID);
        for (int i = 0; i < allChannelName.length; i++) {
            ManageChannel allChannelData = new ManageChannel();
            allChannelData.setName(allChannelName[i]).setId(allChannelID[i]).setType(ManageChannel.ALLCHANNEL_VIEW);
            allChannelList.add(allChannelData);
        }

        ManageChannelAdapter adapter = new ManageChannelAdapter(allChannelList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (allChannelList.get(position).getType().equals(ManageChannel.ALLCHANNEL_TEXT)
                        || allChannelList.get(position).getType().equals(ManageChannel.MYCHANNEL_TEXT)) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });
        channelRecycler.setLayoutManager(gridLayoutManager);
        channelRecycler.setAdapter(adapter);
        channelRecycler.setItemAnimator(new DefaultItemAnimator());
//        channelRecycler.addItemDecoration(new SpaceItemDecoration(8));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MyItemCallback(adapter));
        itemTouchHelper.attachToRecyclerView(channelRecycler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_channel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
