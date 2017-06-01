package com.zmt.e_read.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zmt.e_read.Application.App;
import com.zmt.e_read.Fragment.ImageFragment;
import com.zmt.e_read.Fragment.MovieFragment;
import com.zmt.e_read.Fragment.NewsFragment;
import com.zmt.e_read.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.toolbar) Toolbar toolbar;
    private FragmentManager fragmentManager;
    private NewsFragment newsFragment;
    private ImageFragment imageFragment;
    private MovieFragment movieFragment;
    private MenuItem menuItem;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        accessPermission();
    }

    public void initViews(){
        ButterKnife.bind(this);
        toolbar.setTitle("");
        title.setText(getString(R.string.news));
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,  toolbar, 0, 0);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);
        final Intent intent = new Intent();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_news:
                        fragmentManager.beginTransaction().replace(R.id.content_frame, newsFragment).commit();
                        title.setText(getString(R.string.news));
                        menuItem.setVisible(false);
                        break;
                    case R.id.nav_photo:
                        fragmentManager.beginTransaction().replace(R.id.content_frame, imageFragment).commit();
                        title.setText(getString(R.string.photo));
                        menuItem.setVisible(false);
                        break;
                    case R.id.nav_movie:
                        fragmentManager.beginTransaction().replace(R.id.content_frame, movieFragment).commit();
                        title.setText(getString(R.string.movie));
                        menuItem.setVisible(true);
                        break;
                    case R.id.suggest :
                        intent.setAction(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:zhumintao@xiyou3g.com"));
                        startActivity(intent);
                        break;
                    case R.id.about :
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://xiyoumobile.com"));
                        startActivity(intent);
                        break;
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });

        MenuItem menuItem = navigationView.getMenu().findItem(R.id.night_theme);
        final SwitchCompat switchCompat = (SwitchCompat)MenuItemCompat.getActionView(menuItem);

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (isChecked) {
                    editor.putString("theme", App.NIGHT_THEME);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    setTheme(R.style.NightTheme);
//                    ColorStateList colorStateList = getResources().getColorStateList(R.color.color_fafafa);
//                    navigationView.setItemTextColor(colorStateList);
//                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    initNightTheme();
//                    nightView.setBackgroundResource(R.color.color_8a000000);
                } else {
                    editor.putString("theme", App.DAY_THEME);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    setTheme(R.style.DayTheme);
//                    ColorStateList colorStateList = getResources().getColorStateList(R.color.color_ff000000);
//                    navigationView.setItemTextColor(colorStateList);
                }
//                recreate();
                editor.apply();
            }
        });

        newsFragment = new NewsFragment();
        imageFragment = new ImageFragment();
        movieFragment = new MovieFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame, newsFragment).commit();
    }

    public void accessPermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1 :
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    /**
                     * 授予权限
                     */
                } else {
                    /**
                     * 拒绝授予
                     */

                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                if(System.currentTimeMillis() - exitTime > 2000){
                    exitTime = System.currentTimeMillis();
                    Snackbar.make(coordinatorLayout, "再按一次退出程序", Snackbar.LENGTH_SHORT).show();
                } else {
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        menuItem = menu.findItem(R.id.search);
        menuItem.setVisible(false);
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
            case R.id.search :
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
