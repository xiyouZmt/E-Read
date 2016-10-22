package com.zmt.e_read.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.zmt.e_read.Fragment.PhotoFragment;
import com.zmt.e_read.Module.Image;
import com.zmt.e_read.Module.News;
import com.zmt.e_read.R;
import com.zmt.e_read.Thread.GetBitmap;
import com.zmt.e_read.Utils.ImageUtils;
import com.zmt.e_read.Utils.MD5Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import uk.co.senab.photoview.PhotoView;

public class PhotoActivity extends SwipeBackActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.photoView) PhotoView photoView;
    @BindView(R.id.photo_title) TextView photoTitle;
    private List<Fragment> fragmentList;
    private News imageNews;
    private Image image;
    private String imageUrl = "";
    private File file;
    private int pos = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        initViews();
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new PageChangeListener());
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0x000 :
                    Toast.makeText(PhotoActivity.this, "成功保存至" + file.getPath(), Toast.LENGTH_SHORT).show();
                    break;
                case 0x001 :
                    Toast.makeText(PhotoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public class PageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            pos = position + 1;
            photoTitle.setText(pos + "/" + imageNews.getImageUrl().size() + ' ' + imageNews.getTitle());
            imageUrl = imageNews.getImageUrl().get(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void initViews(){
        ButterKnife.bind(this);
        Intent intent = getIntent();
        toolbar.setTitle("妹子");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentList = new ArrayList<>();
        if(intent.getSerializableExtra(News.IMAGE_NEWS) != null){
            photoView.setVisibility(View.GONE);
            imageNews = (News)getIntent().getSerializableExtra(News.IMAGE_NEWS);
            toolbar.setTitle(imageNews.getTitle());
            photoTitle.setText(pos + "/" + imageNews.getImageUrl().size() + ' ' + imageNews.getTitle());
            imageUrl = imageNews.getImageUrl().get(0);
            for (int i = 0; i < imageNews.getImageUrl().size(); i++) {
                PhotoFragment photoFragment = new PhotoFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(News.IMAGE_COUNT, i + 1);
                bundle.putInt(News.IMAGE_SIZE, imageNews.getImageUrl().size());
                bundle.putString(News.IMAGE_SRC, imageNews.getImageUrl().get(i));
                photoFragment.setArguments(bundle);
                fragmentList.add(photoFragment);
            }
        } else if(intent.getSerializableExtra(Image.TAG) != null){
            viewPager.setVisibility(View.GONE);
            image = (Image) getIntent().getSerializableExtra(Image.TAG);
            /**
             * 显示图片
             */
            imageUrl = image.getImageUrl();
            Glide.with(this).load(image.getImageUrl()).asBitmap().format(DecodeFormat.PREFER_ARGB_8888)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_load_fail).into(photoView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        file = new File(ImageUtils.fileDir,
                MD5Utils.getInstance(imageUrl).hashKeyFromUrl() + ImageUtils.fileSuffix);
        switch (id){
            case R.id.action_save :
                if(file.exists()){
                    Toast.makeText(PhotoActivity.this, "已保存至" + file.getPath(), Toast.LENGTH_SHORT).show();
                } else {
                    GetBitmap getBitmap = new GetBitmap(this, imageUrl, handler);
                    Thread thread = new Thread(getBitmap, "GetBitmap");
                    thread.start();
                }
                break;
            case R.id.action_share :
                if(file.exists()){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    intent.setType("image/jpeg");
                    startActivity(Intent.createChooser(intent, getString(R.string.share_image)));
                } else {
                    Toast.makeText(PhotoActivity.this, "请先保存图片!", Toast.LENGTH_SHORT).show();
                }
                break;
            case android.R.id.home :
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
