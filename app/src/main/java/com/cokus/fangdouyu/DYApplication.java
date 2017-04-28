package com.cokus.fangdouyu;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.cokus.fangdouyu.gen.DaoMaster;
import com.cokus.fangdouyu.gen.DaoSession;
import com.cokus.fangdouyu.global.Constants;
import com.cokus.fangdouyu.util.glide.GlideBitmap;
import com.github.moduth.blockcanary.BlockCanary;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by coku on 16/10/21.
 */
public class DYApplication extends Application {
    public static GlideBitmap glideBitmap;
    private static Context sContext;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    public static DYApplication instances;

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        if(Constants.isDebug) {
//            if (LeakCanary.isInAnalyzerProcess(this)) {
//                // This process is dedicated to LeakCanary for heap analysis.
//                // You should not init your app in this process.
//                return;
//            }
//            LeakCanary.install(this);
//            BlockCanary.install(this, new AppContext()).start();
        }
        init();


    }

    private void init(){
        sContext = getApplicationContext();
        glideBitmap = GlideBitmap.create(getApplicationContext());
    }

    public static DYApplication getInstances(){
        return instances;
    }


    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(this, "douyu-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }
    public DaoSession getDaoSession() {
        return mDaoSession;
    }
    public SQLiteDatabase getDb() {
        return db;
    }



    public static Context getAppContext() {
        return sContext;
    }
}
