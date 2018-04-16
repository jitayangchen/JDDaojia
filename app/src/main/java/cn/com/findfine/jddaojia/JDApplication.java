package cn.com.findfine.jddaojia;

import android.app.Application;

/**
 * Created by yangchen on 2018/3/29.
 */

public class JDApplication extends Application {

    private static JDApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

    }

    public static JDApplication getApplication() {
        return application;
    }



}
