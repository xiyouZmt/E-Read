package com.zmt.e_read.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.zmt.e_read.R;
import com.zmt.e_read.libcore.io.DiskLruCache;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Test extends AppCompatActivity {

    private final int NODE_COUNT = 1;
    private final int DISK_CACHE_INDEX = 0;
    private int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private int cacheSize = maxMemory / 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(maxMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return super.sizeOf(key, value);
            }
        };

        /**
         * 向磁盘中写入缓存
         */
        final long DISK_SIZE = 1024 * 1024 * 50;
        DiskLruCache diskLruCache = null;
        String path = "android/data/com.zmt.E-Read/cache";
        File diskCacheDir = new File(Environment.getExternalStorageDirectory() + "/" + path);
        if(!diskCacheDir.exists()){
            diskCacheDir.mkdir();
        }
        String url = "http://cms-bucket.nosdn.127.net/64f24b53da254cad96e9cd0bb13a8cac20161005073315.jpeg";
        try {
            diskLruCache = DiskLruCache.open(diskCacheDir, 1, NODE_COUNT, DISK_SIZE);
            String key = hashKeyFromUrl(url);
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            if(editor != null){
                OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
                /**
                 * 下载文件到输出流
                 *
                 *
                 */
                editor.commit();
                diskLruCache.flush();
            }
        } catch (IOException e) {
            Log.e("diskLruCache", e.toString());
        }

        /**
         * 从磁盘中读取缓存
         */
        Bitmap bitmap ;
        String key = hashKeyFromUrl(url);
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if(snapshot != null){
                FileInputStream inputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
                FileDescriptor fileDescriptor = inputStream.getFD();
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                lruCache.put(key, bitmap);
                diskLruCache.remove(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private String hashKeyFromUrl(String url){
        String cacheKry = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            cacheKry = bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return cacheKry;
    }

    private String bytesToHexString(byte [] bytes){
        StringBuilder builder = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xff & aByte);
            if (hex.length() == 1) {
                builder.append('0');
            }
            builder.append(hex);
        }
        return builder.toString();
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;
        if(width > reqWidth || height > reqHeight){
            final int halfWidth = width / 2;
            final int halfHeight = height / 2;
            while (halfWidth / inSampleSize >= reqWidth
                    || halfHeight / inSampleSize >= reqHeight){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
