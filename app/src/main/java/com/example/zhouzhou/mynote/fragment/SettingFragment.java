package com.example.zhouzhou.mynote.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.example.zhouzhou.mynote.R;

/**
 * Created by zhouzhou on 2017/9/20.
 */

public class SettingFragment extends PreferenceFragment{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
