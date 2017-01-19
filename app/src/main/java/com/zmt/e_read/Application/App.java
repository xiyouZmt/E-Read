package com.zmt.e_read.Application;

        import android.app.Application;
        import android.content.SharedPreferences;

/**
 * Created by MintaoZhu on 2016/10/25.
 */
public class App extends Application {

    public final static String DAY_THEME = "DAY_THEME";

    public final static String NIGHT_THEME = "NIGHT_THEME";

    public static String MyTheme = DAY_THEME;

    @Override
    public void onCreate() {
        super.onCreate();
//        getLocalTheme();
    }

    public void getLocalTheme(){
        SharedPreferences sharedPreferences = getSharedPreferences("theme", MODE_PRIVATE);
        MyTheme = sharedPreferences.getString("theme", DAY_THEME);
    }
}
