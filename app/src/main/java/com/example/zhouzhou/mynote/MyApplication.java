package com.example.zhouzhou.mynote;

import android.app.Application;

import com.example.zhouzhou.mynote.config.Constants;

import cn.bmob.v3.Bmob;

/**
 * Created by zhouzhou on 2017/9/20.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化Bmob
        Bmob.initialize(this, Constants.BMOB_API_KEY);
    }
}
