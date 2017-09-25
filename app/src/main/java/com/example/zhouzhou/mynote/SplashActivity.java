package com.example.zhouzhou.mynote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import cn.bmob.v3.BmobUser;
/**
 * Created by zhouzhou on 2017/9/20.
 */
public class SplashActivity extends AppCompatActivity {
private ImageView mIvSplash;
    public static final String SEND_USER_NAME="send_user_name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mIvSplash=(ImageView)findViewById(R.id.id_iv_splash);
        //实现渐变效果
        Animation animation=new AlphaAnimation(0.5f,1f);
        animation.setDuration(3000);
        mIvSplash.startAnimation(animation);
        //动画结束自动启动登录界面或主界面
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //退出当前用户
                BmobUser.logOut(SplashActivity.this);
                //判断是否有用户登录
                BmobUser user=BmobUser.getCurrentUser(SplashActivity.this);
                //已经登录
                if (user!=null){
                    String userName=user.getUsername();
                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                    intent.putExtra(SEND_USER_NAME,userName);
                    startActivity(intent);
                }else{
                    //启动登录界面
                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
