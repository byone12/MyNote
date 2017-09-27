package com.example.zhouzhou.mynote;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhouzhou.mynote.config.Constants;
import com.example.zhouzhou.mynote.cube.Kube;
import com.example.zhouzhou.mynote.fragment.AllNotesFragment;
import com.example.zhouzhou.mynote.fragment.SearchNoteFragment;
import com.example.zhouzhou.mynote.fragment.SettingFragment;
import com.example.zhouzhou.mynote.service.AutoSyncService;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
/**
 * Created by zhouzhou on 2017/9/20.
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,NavigationView.OnNavigationItemSelectedListener{
private Toolbar mToolbar;
    private ActionBarDrawerToggle mToggle;
    private FrameLayout mFlContent;
    private NavigationView mNvMenu;
    private DrawerLayout mDlLayout;
    private TextView mTvUserName;
    private View mHeaderView;
    private Fragment mFragments[]=new Fragment[3];
    private String mUserName;
    private long curTimeMills;
    //是否自动启动service执行计划任务
    public static boolean IS_SYNC=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "438a594dd134c7db048e436740685e4e");
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        mToolbar.setTitle("My Note");
        setSupportActionBar(mToolbar);
        //获取配置信息
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        IS_SYNC=pref.getBoolean("auto_sync",false);
        mUserName=getIntent().getStringExtra(SplashActivity.SEND_USER_NAME);
        mFragments[0]=new AllNotesFragment();
        mFragments[1] = new SearchNoteFragment();
        mFragments[2] = new SettingFragment();
        initView();
        showFragment(mFragments[0]);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initView() {
        mDlLayout = (DrawerLayout) findViewById(R.id.id_dl_main_layout);
        mFlContent = (FrameLayout) findViewById(R.id.id_fl_main_content);
        mNvMenu = (NavigationView) findViewById(R.id.id_nav_menu);
        // 获取HeaderView
        mHeaderView = mNvMenu.getHeaderView(0);
        mTvUserName = (TextView) mHeaderView.findViewById(R.id.id_tv_username);
        mNvMenu.setNavigationItemSelectedListener(this);
        mToggle = new ActionBarDrawerToggle(this, mDlLayout, mToolbar, R.string.app_name, R.string.app_name);
        mToggle.syncState();
        mDlLayout.setDrawerListener(mToggle);
        mTvUserName.setText(mUserName);
    }


    /**
     * 显示指定的Fragment
     */
    private void showFragment(android.app.Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.id_fl_main_content, fragment).commit();
    }


    /**
     * 添加菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_menu_add_note:
                Toast.makeText(this, "添加笔记", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, NoteDetailActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 切换到相应的Fragment
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        getFragmentManager().beginTransaction()
                .replace(R.id.id_fl_main_content, mFragments[position]).commit();

        mDlLayout.closeDrawers();
    }


    /**
     * 点击侧滑触发相应的事件
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_all_notes:
                showFragment(mFragments[0]);
                Toast.makeText(this, "全部笔记", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_search:
                showFragment(mFragments[1]);
                Toast.makeText(this, "搜索笔记", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_setting:
                showFragment(mFragments[2]);
                Toast.makeText(this, "自动同步", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                logout();
                break;
            case R.id.nav_about:
                about();
                break;
            default:
                Toast.makeText(this, "功能开发中", Toast.LENGTH_SHORT).show();
                break;
        }
        // 关闭菜单
        mDlLayout.closeDrawer(GravityCompat.START);
        // 表示已经处理完毕点击事件
        return true;
    }
/**
 * 关于
 */
private void about(){
Intent intent=new Intent(this,Kube.class);
    startActivity(intent);
}
    /**
     * 退出当前用户
     */
    private void logout() {
        BmobUser.logOut(this);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 单击后退键时,提示用户是否退出
     */
    @Override
    public void onBackPressed() {
        //curTimeMills = System.currentTimeMillis();
        if (mDlLayout.isDrawerOpen(GravityCompat.START)) {
            mDlLayout.closeDrawer(GravityCompat.START);
        } else {
            // 关闭程序
            exitAPP();
        }
    }

    /**
     * 两秒内单击两下即可关闭APP
     */
    private void exitAPP() {

        if (System.currentTimeMillis() - curTimeMills > 2000) {
            Snackbar.make(mDlLayout, "再单击一下即可退出", Snackbar.LENGTH_SHORT).show();
            curTimeMills = System.currentTimeMillis();
        } else {
            System.exit(0);
        }

    }
}
