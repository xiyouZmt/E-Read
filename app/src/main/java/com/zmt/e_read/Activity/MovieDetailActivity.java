package com.zmt.e_read.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.zmt.e_read.Module.Movie;
import com.zmt.e_read.R;
import com.zmt.e_read.Thread.GetData;
import com.zmt.e_read.Utils.Analyse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.movie_image) ImageView movie_image;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindViews({R.id.movie_translate_name, R.id.movie_name, R.id.movie_year, R.id.movie_country,
    R.id.movie_style, R.id.movie_language, R.id.movie_captions, R.id.movie_score, R.id.movie_form,
    R.id.movie_size, R.id.movie_bulk, R.id.movie_length, R.id.movie_director, R.id.movie_actor,
            R.id.movie_introduce}) TextView [] textViews;
    @BindView(R.id.preview) ImageView preview;
    private Movie movie;
    private Map<String, String> map;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        initViews();
        GetData getNewsData = new GetData(movie.getUrl(), handler, Movie.TAG);
        Thread thread = new Thread(getNewsData, "GetData");
        thread.start();
    }

    private final Handler handler = new Handler(){
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
                        analyse.analyseMovieDetail(object.toString(), map);
                        if(map.get("movie_image") != null){
                            Picasso.with(MovieDetailActivity.this).load(map.get("movie_image"))
                                    .placeholder(R.drawable.ic_loading)
                                    .error(R.drawable.ic_load_fail)
                                    .into(movie_image);
                        }
                        if(map.get("movie_preview") != null){
                            Glide.with(MovieDetailActivity.this).load(map.get("movie_preview")).asBitmap()
                                    .placeholder(R.drawable.ic_loading)
                                    .format(DecodeFormat.PREFER_ARGB_8888)
                                    .error(R.drawable.ic_load_fail)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(preview);
                        }
                        String movie_content = map.get("movie_content");
                        int start = movie_content.indexOf('◎');
                        int end = 0;
                        for (int i = start + 1; i < movie_content.length(); i++) {
                            if(movie_content.charAt(i) == '◎'){
                                end = i;
                                list.add(movie_content.substring(start + 1, end));
                                start = i;
                            }
                            if(i == movie_content.length() - 1){
                                end = i+1;
                                break;
                            }
                        }
                        list.add(movie_content.substring(start + 1, end));
                        int i = 0;
                        for(TextView textView : textViews){
                            if(i == 0){
                                textView.setText(movie.getName());
                            } else {
                                textView.setText(list.get(i));
                            }
                            i++;
                            if(i >= list.size()){
                                break;
                            }
                        }
//                        toolbar.setTitle(list.get(0));
//                        collapsingToolbarLayout.setTitle(list.get(0));
                        break;
                }
            }
        }
    };

    public void initViews(){
        ButterKnife.bind(this);
        map = new HashMap<>();
        list = new ArrayList<>();
        movie = (Movie)getIntent().getSerializableExtra(Movie.TAG);
//        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle(movie.getName());
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}