package com.zmt.e_read.Activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentManager;

import com.zmt.e_read.Fragment.ImageFragment;
import com.zmt.e_read.Fragment.NewsFragment;
import com.zmt.e_read.Fragment.VideoFragment;
import com.zmt.e_read.R;
import com.zmt.e_read.Utils.StatusBarCompat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.toolBar) Toolbar toolbar;
    @BindView(R.id.navigationView) NavigationView navigationView;
//    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    private FragmentManager fragmentManager;
    private NewsFragment newsFragment;
    private ImageFragment imageFragment;
    private VideoFragment videoFragment;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    public void initViews(){
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
//        StatusBarCompat.compat(this, getResources().getColor(R.color.color_039be5));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,  toolbar, 0, 0);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_news:
                        fragmentManager.beginTransaction().replace(R.id.content_frame, newsFragment).commit();
                        break;
                    case R.id.nav_photo:
                        fragmentManager.beginTransaction().replace(R.id.content_frame, imageFragment).commit();
                        break;
                    case R.id.nav_video:
                        fragmentManager.beginTransaction().replace(R.id.content_frame, videoFragment).commit();
                        break;
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });

        newsFragment = new NewsFragment();
        imageFragment = new ImageFragment();
        videoFragment = new VideoFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame, newsFragment).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else {
                if(System.currentTimeMillis() - exitTime > 2000){
                    exitTime = System.currentTimeMillis();
//                    Snackbar.make(coordinatorLayout, "再按一次退出程序", Snackbar.LENGTH_SHORT);
                } else {
                    finish();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.slidemenu, menu);
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
            case R.id.nav_news :
                fragmentManager.beginTransaction().replace(R.id.content_frame, newsFragment).commit();
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_photo :
                fragmentManager.beginTransaction().replace(R.id.content_frame, imageFragment).commit();
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_video :
                fragmentManager.beginTransaction().replace(R.id.content_frame, videoFragment).commit();
                drawerLayout.closeDrawers();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
