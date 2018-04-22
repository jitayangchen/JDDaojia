package cn.com.findfine.jddaojia;

import android.app.Application;


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
