package com.zmt.e_read.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zmt.e_read.Model.ChannelData;
import com.zmt.e_read.Model.News;
import com.zmt.e_read.R;
import com.zmt.e_read.Thread.GetNewsData;
import com.zmt.e_read.Utils.Analyse;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.IOException;
import java.net.HttpRetryException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetail extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.news_image) ImageView news_image;
    @BindView(R.id.source) TextView news_source;
    @BindView(R.id.news_content) TextView news_content;
    private News news;
    private String contentUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initViews();
        GetNewsData getNewsData = new GetNewsData(contentUrl, handler);
        Thread thread = new Thread(getNewsData, "GetNewsData");
        thread.start();
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
                        Analyse analyse = new Analyse();
                        news_source.setText(news.getSource() + " " + news.getTime());
                        String news_detail = analyse.analyseNewsDetail(news.getDocId(), object.toString());
                        news_content.setText(Html.fromHtml(news_detail, new com.zmt.e_read.Utils.HtmlHttpImageGetter(news_content), null));
                        break;
                }
            }
        }
    };

    public void initViews(){
        ButterKnife.bind(this);
        news = (News) getIntent().getSerializableExtra(News.TEXT_NEWS);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        /**
         * 设置文章标题
         */
        collapsingToolbarLayout.setTitle(news.getTitle());
        /**
         * 设置文章封面
         */
        Glide.with(this).load(news.getImageUrl().get(0)).asBitmap()
                .placeholder(R.drawable.ic_loading)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .error(R.drawable.ic_load_fail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(news_image);
        contentUrl = ChannelData.NEWS_DETAIL + news.getDocId() + ChannelData.END_DETAIL;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, getShareContent());
                startActivity(Intent.createChooser(intent, getTitle()));
            }
        });
    }

    public String getShareContent(){
        return getString(R.string.share, news.getTitle(), news.getContentUrl());
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
