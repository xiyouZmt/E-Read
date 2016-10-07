package com.zmt.e_read.Utils;

/**
 * Created by Dangelo on 2016/10/6.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URL;

public class HtmlHttpImageGetter implements Html.ImageGetter {
    TextView container;
    URI baseUri;
    boolean matchParentWidth;

    public HtmlHttpImageGetter(TextView textView) {
        this.container = textView;
        this.matchParentWidth = true;
    }

    public HtmlHttpImageGetter(TextView textView, String baseUrl) {
        this.container = textView;
        if(baseUrl != null) {
            this.baseUri = URI.create(baseUrl);
        }

    }

    public HtmlHttpImageGetter(TextView textView, String baseUrl, boolean matchParentWidth) {
        this.container = textView;
        this.matchParentWidth = matchParentWidth;
        if(baseUrl != null) {
            this.baseUri = URI.create(baseUrl);
        }

    }

    public Drawable getDrawable(String source) {
        HtmlHttpImageGetter.UrlDrawable urlDrawable = new HtmlHttpImageGetter.UrlDrawable();
        HtmlHttpImageGetter.ImageGetterAsyncTask asyncTask = new HtmlHttpImageGetter.ImageGetterAsyncTask(urlDrawable, this, this.container, this.matchParentWidth);
        asyncTask.execute(new String[]{source});
        return urlDrawable;
    }

    public class UrlDrawable extends BitmapDrawable {
        protected Drawable drawable;

        public UrlDrawable() {
        }

        public void draw(Canvas canvas) {
            if(this.drawable != null) {
                this.drawable.draw(canvas);
            }

        }
    }

    private static class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        private final WeakReference<UrlDrawable> drawableReference;
        private final WeakReference<HtmlHttpImageGetter> imageGetterReference;
        private final WeakReference<View> containerReference;
        private TextView container;
        private String source;
        private boolean matchParentWidth;
        private float scale;

        public ImageGetterAsyncTask(HtmlHttpImageGetter.UrlDrawable d, HtmlHttpImageGetter imageGetter, View container, boolean matchParentWidth) {
            this.drawableReference = new WeakReference(d);
            this.imageGetterReference = new WeakReference(imageGetter);
            this.container = (TextView)container;
            this.containerReference = new WeakReference(container);
            this.matchParentWidth = matchParentWidth;
        }

        protected Drawable doInBackground(String... params) {
            this.source = params[0];
            return this.fetchDrawable(this.source);
        }

        protected void onPostExecute(Drawable result) {
            if(result == null) {
                Log.w("HtmlTextView", "Drawable result is null! (source: " + this.source + ")");
            } else {
                HtmlHttpImageGetter.UrlDrawable urlDrawable = (HtmlHttpImageGetter.UrlDrawable)this.drawableReference.get();
                if(urlDrawable != null) {
                    urlDrawable.setBounds(0, 0, (int)((float)result.getIntrinsicWidth() * this.scale), (int)((float)result.getIntrinsicHeight() * this.scale));
//                    urlDrawable.setBounds(0, 0, container.getWidth(), calculatePicHeight(result, container));
//                    result.setBounds(0, 0, container.getWidth(), calculatePicHeight(result, container));
                    urlDrawable.drawable = result;
                    HtmlHttpImageGetter imageGetter = (HtmlHttpImageGetter)this.imageGetterReference.get();
                    if(imageGetter != null) {
                        imageGetter.container.invalidate();
                        imageGetter.container.setText(imageGetter.container.getText());
                    }
                }
            }
        }

        private int calculatePicHeight(Drawable drawable, TextView textView) {
            float imgWidth = drawable.getIntrinsicWidth();
            float imgHeight = drawable.getIntrinsicHeight();
            float rate = imgHeight / imgWidth;
            return (int) (textView.getWidth() * rate);
        }

        public Drawable fetchDrawable(String urlString) {
            try {
                InputStream e = this.fetch(urlString);
                Drawable drawable = Drawable.createFromStream(e, "src");
                this.scale = this.getScale(drawable);
                drawable.setBounds(0, 0, (int)((float)drawable.getIntrinsicWidth() * this.scale), (int)((float)drawable.getIntrinsicHeight() * this.scale));
                return drawable;
            } catch (Exception var4) {
                return null;
            }
        }

        private float getScale(Drawable drawable) {
            View container = (View)this.containerReference.get();
            if(this.matchParentWidth && container != null) {
                float maxWidth = (float)container.getWidth();
                float originalDrawableWidth = (float)drawable.getIntrinsicWidth();
                return maxWidth / originalDrawableWidth;
            } else {
                return 1.0F;
            }
        }

        private InputStream fetch(String urlString) throws IOException {
            HtmlHttpImageGetter imageGetter = (HtmlHttpImageGetter)this.imageGetterReference.get();
            if(imageGetter == null) {
                return null;
            } else {
                URL url;
                if(imageGetter.baseUri != null) {
                    url = imageGetter.baseUri.resolve(urlString).toURL();
                } else {
                    url = URI.create(urlString).toURL();
                }

                return (InputStream)url.getContent();
            }
        }
    }
}

