package com.zmt.e_read.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.zmt.e_read.Adapter.AdapterInterface.OnItemClickListener;
import com.zmt.e_read.Adapter.MovieAdapter;
import com.zmt.e_read.Module.Movie;
import com.zmt.e_read.Module.MovieChannel;
import com.zmt.e_read.R;
import com.zmt.e_read.Thread.GetData;
import com.zmt.e_read.Utils.Analyse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SearchActivity extends SwipeBackActivity implements OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edit_search)
    EditText edit_search;
    @BindView(R.id.speech_recognize)
    ImageView speech_recognize;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<Movie> movieList;
    private MovieAdapter adapter;
    private ProgressDialog progressDialog;
    private LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Object object = msg.obj;
            if(object != null){
                switch (object.toString()){
                    case "network error" :
                        Snackbar.make(recyclerView, "网络连接错误!", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "server error" :
                        Snackbar.make(recyclerView, "服务器连接错误!", Snackbar.LENGTH_SHORT).show();
                        break;
                    default :
                        try{
                            Analyse analyse = new Analyse();
                            analyse.analyseMovieList(false, MovieChannel.NewestFilm, Movie.SEARCH, object.toString(), movieList);
                            if(adapter == null){
                                adapter = new MovieAdapter(movieList, SearchActivity.this);
                            }
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        } catch (Exception e){
                            Log.e("error", e.toString());
                        }
                        break;
                }
                progressDialog.dismiss();
            }
        }
    };

    @OnClick(R.id.speech_recognize)
    public void onClick(View v){

    }

    public void initViews(){
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        movieList = new ArrayList<>();
        manager = new LinearLayoutManager(
                SearchActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载...");
        progressDialog.setCancelable(false);

//        edit_search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }

    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Movie.TAG, movieList.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case android.R.id.home :
                finish();
                break;
            case R.id.action_search :
                String keyWord;
                if(!(keyWord = edit_search.getText().toString()).equals("")
                        && keyWord.getBytes().length >= 4){
//                    progressBar.setVisibility(View.VISIBLE);
                    progressDialog.show();
                    try {
                        keyWord = URLEncoder.encode(keyWord, "gb2312");
                        GetData getData = new GetData(Movie.search_url + keyWord, handler, Movie.TAG);
                        Thread thread = new Thread(getData, "GetThread");
                        thread.start();
                    } catch (UnsupportedEncodingException e) {
                        Log.e("encode error", e.toString());
                    }
                } else {
                    Snackbar.make(recyclerView, "关键字长度小于四个字节!", Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
