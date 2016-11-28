package com.zmt.e_read.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zmt.e_read.Adapter.DragAdapter;
import com.zmt.e_read.DragGridView.DragGridView;
import com.zmt.e_read.Module.ChannelData;
import com.zmt.e_read.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddChannel extends AppCompatActivity implements AdapterView.OnItemClickListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.myChannel)
    DragGridView myChannel;
    @BindView(R.id.allChannel)
    DragGridView allChannel;
    private List<ChannelData> myChannelList;
    private List<ChannelData> allChannelList;
    private DragAdapter myAdapter;
    private DragAdapter allAdapter;
    private boolean isMove = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_channel);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if(isMove){
            return;
        }
//        final ImageView imageView = getView(view);
        ChannelData channelData = new ChannelData();
        try{
            switch (parent.getId()){
                case R.id.myChannel :
                    allChannelList.add(channelData);
                    allAdapter.notifyDataSetChanged();
                    TextView startText = (TextView) myAdapter.getView(position);
                    TextView endText = (TextView) allAdapter.getView(allChannelList.size() - 1);
                    Log.e("startText", startText.getText().toString());
                    Log.e("endText", endText.getText().toString());
//                    float startTextX = startText.getX();
//                    float startTextY = startText.getY();
//                    float endTextX = endText.getX();
//                    float endTextY = endText.getY();
                    int [] startLocation = new int[2];
                    startText.getLocationInWindow(startLocation);
                    int [] endLocation = new int[2];
                    endText.getLocationInWindow(endLocation);
                    float distanceX = endLocation[0] - startLocation[0];
                    float distanceY = endLocation[1] - startLocation[1];
                    TranslateAnimation moveAnimation = new TranslateAnimation(
                            startLocation[0], endLocation[0], startLocation[1], endLocation[1]);
                    moveAnimation.setDuration(300L);
                    moveAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            myChannelList.remove(position);
                            myAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

//                if(imageView != null){
//                    TextView channelText = (TextView) findViewById(R.id.text_item);
//                    final int [] startLocation = new int[2];
//                    channelText.getLocationInWindow(startLocation);
//                    final ChannelData channelData = myChannelList.get(position);
//                    myChannelList.remove(channelData);
//                    new Handler().postDelayed(new Runnable() {
//                        public void run() {
//                            try {
//                                int[] endLocation = new int[2];
//                                allChannel.getChildAt(allChannel.getLastVisiblePosition()).getLocationInWindow(endLocation);
//                                moveAnim(imageView, startLocation, endLocation, myChannel);
//                                myAdapter.removeItem(position);
//                            } catch (Exception e) {
//                                Log.e("error", e.toString());
//                            }
//                        }
//                    }, 50L);
//                }
                    break;
                case R.id.allChannel :
//                myChannelList.add(channelData);
//                myAdapter.notifyDataSetChanged();
                    break;
            }
        } catch (Exception e){
            Log.e("animator error", e.toString());
        }
    }

    private void moveAnim(View moveView, int[] startLocation, int[] endLocation, final GridView clickGridView) {
        int[] initLocation = new int[2];
        moveView.getLocationInWindow(initLocation);
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1], endLocation[1]);
        moveAnimation.setDuration(300L);

        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                if (clickGridView instanceof DragGridView) {
//                    allAdapter.setVisible(true);
                    allAdapter.notifyDataSetChanged();
//                    myAdapter.remove();
                } else {
//                    myAdapter.setVisible(true);
                    myAdapter.notifyDataSetChanged();
//                    allAdapter.remove();
                }
                isMove = false;
            }
        });
    }

    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }

    public void initViews() {
        ButterKnife.bind(this);
        toolbar.setTitle("频道管理");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        /**
         * user channel data
         */
        String [] myChannelName = getResources().getStringArray(R.array.channelName);
        String [] myChannelID = getResources().getStringArray(R.array.channelID);
        myChannelList = new ArrayList<>();
        for (int i = 0; i < myChannelName.length; i++) {
            ChannelData channelData = new ChannelData();
            channelData.setName(myChannelName[i]).setId(myChannelID[i]);
            myChannelList.add(channelData);
        }
        myAdapter = new DragAdapter(this, myChannelList);
        myChannel.setAdapter(myAdapter);
        myChannel.setOnItemClickListener(this);

        /**
         * all channel data
         */
        String[] allChannelName = getResources().getStringArray(R.array.allChannelName);
        String[] allChannelID = getResources().getStringArray(R.array.allChannelID);
        allChannelList = new ArrayList<>();
        for (int i = 0; i < allChannelName.length; i++) {
            ChannelData channelData = new ChannelData();
            channelData.setName(allChannelName[i]).setId(allChannelID[i]);
            allChannelList.add(channelData);
        }
        allAdapter = new DragAdapter(this, allChannelList);
        allChannel.setAdapter(allAdapter);
        allChannel.setOnItemClickListener(this);
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
