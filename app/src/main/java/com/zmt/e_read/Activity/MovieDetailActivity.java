package com.zmt.e_read.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.zmt.e_read.Module.Movie;
import com.zmt.e_read.R;
import com.zmt.e_read.Thread.GetData;
import com.zmt.e_read.Utils.AnalyseUtils;
import com.zmt.e_read.Utils.ThunderUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class MovieDetailActivity extends SwipeBackActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.movie_image) ImageView movie_image;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindViews({R.id.movie_translate_name, R.id.movie_name, R.id.movie_another_name, R.id.movie_year,
            R.id.movie_country, R.id.movie_style, R.id.movie_language, R.id.movie_captions, R.id.release_date,
            R.id.movie_score_IMDb, R.id.movie_score_douBan, R.id.movie_form, R.id.movie_size, R.id.movie_bulk,
            R.id.movie_length, R.id.movie_director, R.id.movie_actor, R.id.movie_introduce}) TextView [] textViews;
    @BindViews({R.id.linear_translate_name, R.id.linear_movie_name, R.id.linear_another_name, R.id.linear_movie_year,
            R.id.linear_movie_country, R.id.linear_movie_style, R.id.linear_movie_language, R.id.linear_movie_captions, R.id.linear_release_date,
            R.id.linear_score_IMDb, R.id.linear_score_douBan, R.id.linear_movie_form, R.id.linear_movie_size, R.id.linear_movie_bulk,
            R.id.linear_movie_length, R.id.linear_movie_director, R.id.linear_movie_actor, R.id.linear_movie_introduce}) LinearLayout [] linearLayouts;
    @BindView(R.id.preview) ImageView preview;
    private Movie movie;
    private Map<String, String> map;
    private List<String> list;
    private String [] movie_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        initViews();
        GetData getNewsData = new GetData(movie.getUrl(), handler, Movie.TAG);
        Thread thread = new Thread(getNewsData, "GetData");
        thread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(list != null && list.size() != 0){
            toolbar.setTitle(movie.getName());
            collapsingToolbarLayout.setTitle(list.get(0).substring(5));
        }
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
                        AnalyseUtils analyse = new AnalyseUtils();
                        analyse.analyseMovieDetail(object.toString(), map);
                        showMovieImage(map);
                        ignorePrefix(list, map);
                        showMovieInfos(list, textViews);
                        break;
                }
            }
        }
    };

    private void showMovieImage(Map<String, String> map){
        if(map.get("movie_image") != null){
            /**
             * Glide会影响图片沉浸式显示
             */
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
    }

//    private void showMovieInfo(List<String> list, TextView [] textViews){
//        int i = 0;
//        int start, end;
//        /**
//         * 设置toolbar标题
//         */
//        collapsingToolbarLayout.setTitle(list.get(0).substring(5));
//        for(TextView textView : textViews){
//            String value = list.get(i);
//            if(i == 0){
//                /**
//                 * 片名
//                 */
//                textView.setText(movie.getName());
//            } else if(i == 7 || i == 8){
//                /**
//                 * IMDb和豆瓣评分处理
//                 */
//                if(value.contains("users") || value.contains("votes")){
//                    end =  0;
//                    boolean first = true;
//                    for (int j = 0; j < value.length(); j++) {
//                        if(first && value.charAt(j) == ' '){
//                            first = false;
//                        } else if(!first && value.charAt(j) != ' '){
//                            end = j;
//                            break;
//                        }
//                    }
//                    textView.setText(value.substring(end));
//                } else {
//                    if(linear_grade.getVisibility() == View.VISIBLE){
//                        linear_grade.setVisibility(View.GONE);
//                        i--;
//                    } else {
//                        textView.setText(value.substring(5));
//                    }
//                }
//            } else if(i == list.size() - 2){
//                /**
//                 * actor换行处理
//                 */
//                start = end =  0;
//                boolean first = true;
//                Log.e("value", value);
//                for (int j = 0; j < value.length(); j++) {
//                    Log.e(String.valueOf(j) + "--->", String.valueOf(value.charAt(j)));
//                    if(first && value.charAt(j) == '　'){
//                        start = j;
//                        first = false;
//                    } else if(!first && value.charAt(j) != '　'){
//                        end = j;
//                        first = true;
//                        if(end - start > 2){
//                            break;
//                        }
//                    }
//                }
//                value = value.replace(value.substring(start, end), "\n");
//                textView.setText(value.substring(5));
//            } else if(i == list.size() - 1){
//                /**
//                 * 忽略简介缩进
//                 */
//                textView.setText(value.substring(7));
//            } else {
//                textView.setText(value.substring(5));
//            }
//            i++;
//            if(i >= list.size()){
//                break;
//            }
//        }
//    }

    public void showMovieInfos(List<String> list, TextView [] textViews){
        int movieInfoPos = 0;
        int displace = 0;
        int start, end;
        collapsingToolbarLayout.setTitle(list.get(0).substring(5));
        for (int i = 0; i < list.size(); i++) {
            String temp = list.get(i);
            while (true) {
                if(movieInfoPos == movie_info.length){
                    break;
                }
                String info = temp.substring(0, 4);
                if(info.equals(movie_info[movieInfoPos]) || info.equals("国　　家") || info.equals("地　　区") || info.equals("类　　型")){
                    String content ;
                    switch (movie_info[movieInfoPos]) {
                        case "IMDb":
                            content = temp.substring(7);
                            break;
                        case "主　　演":
                            /**
                             * actor换行处理
                             */
                            start = end = 0;
                            boolean first = true;
                            for (int j = 0; j < temp.length(); j++) {
                                Log.e(String.valueOf(j) + "--->", String.valueOf(temp.charAt(j)));
                                if (first && temp.charAt(j) == '　') {
                                    start = j;
                                    first = false;
                                } else if (!first && temp.charAt(j) != '　') {
                                    end = j;
                                    first = true;
                                    if (end - start > 2) {
                                        break;
                                    }
                                }
                            }
                            temp = temp.replace(temp.substring(start, end), "\n");
                            content = temp.substring(5);
                            break;
                        case "简　　介" :
                            /**
                             * 忽略缩进
                             */
                            content = temp.substring(7);
                            break;
                        default:
                            content = temp.substring(5);
                            break;
                    }
                    textViews[i + displace].setText(content);
                    movieInfoPos ++;

                    break;
                } else {
                    /**
                     * gone掉对应布局
                     */
                    linearLayouts[i + displace].setVisibility(View.GONE);
                    displace ++;
                    movieInfoPos ++;
                }
            }
        }
    }

    private void ignorePrefix(List<String> list, Map<String, String> map){
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
    }

    public void initViews(){
        ButterKnife.bind(this);
        map = new HashMap<>();
        list = new ArrayList<>();
        movie = (Movie)getIntent().getSerializableExtra(Movie.TAG);
//        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
//        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        movie_info = getResources().getStringArray(R.array.movie_info);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String downloadUrl = map.get("movie_downloadUrl");
                ThunderUtils thunderUtils = ThunderUtils.getInstance(MovieDetailActivity.this);
                if(thunderUtils.isInstalled()){
                    thunderUtils.downloadFile(downloadUrl);
                } else {
                    Snackbar.make(fab, "该影片链接为迅雷P2P资源,须安装迅雷下载工具才能进行下载", Snackbar.LENGTH_SHORT).show();
                }
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