package com.example.zhouzhou.mynote;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
/**
 * Created by zhouzhou on 2017/9/20.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText mEtUsername;
    private EditText mEtPwd;
    private View mProgressView;
    private View mLoginFromView;
    private Button mBtnLogin;
    private CheckBox rememberPass;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
    }
    //初始化组件
    private void initView(){
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        mEtPwd=(EditText)findViewById(R.id.id_et_password);
        mEtUsername=(EditText)findViewById(R.id.id_et_username);
        mBtnLogin=(Button)findViewById(R.id.id_btn_login);
        mProgressView=findViewById(R.id.id_pb_loading);
        mLoginFromView=findViewById(R.id.id_lv_login_form);
        rememberPass=(CheckBox) findViewById(R.id.remember_pass);
        boolean isRemember=pref.getBoolean("remember_password",false);
        if (isRemember){
            String account=pref.getString("account","");
            String passwrod=pref.getString("password","");
            mEtUsername.setText(account);
            mEtPwd.setText(passwrod);
            rememberPass.setChecked(true);
        }
    }
    //初始化事件
    private void initEvent(){
        mEtPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i==R.id.login||i== EditorInfo.IME_NULL){
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }
    /**
     * 接收登录的结果并判断错误
     */
    private void attemptLogin(){
        //重置 errors
        mEtUsername.setError(null);
        mEtPwd.setError(null);
        //保存输入的账号密码
        String userName=mEtUsername.getText().toString();
        String password=mEtPwd.getText().toString();
        boolean cancel=false;
        View focusView=null;
        //检查密码
        if(TextUtils.isEmpty(password)||!isPasswordValid(password)){
            mEtPwd.setError("密码必须大于5位");
            focusView=mEtPwd;
            cancel=true;
        }
        //检查用户名
        if(TextUtils.isEmpty(userName)){
            mEtUsername.setError("用户名的必填项");
            focusView=mEtUsername;
            cancel=true;
        }else if(!isUserNameValid(userName)){
            mEtUsername.setError("用户名必须大于4位");
            focusView=mEtUsername;
            cancel=true;
        }
        if(cancel){
            focusView.requestFocus();
        }else{
            editor=pref.edit();
            if (rememberPass.isChecked()){
                editor.putBoolean("remember_password",true);
                editor.putString("account",userName);
                editor.putString("password",password);
            }else{
                editor.clear();
            }
            editor.apply();
            showProgress(true);
            loginInBmob(userName,password);
        }
    }
    /**
     * 向Bmob提交登录数据
     */
    private void loginInBmob(final String name,String pwd){
        final BmobUser user=new BmobUser();
        user.setUsername(name);
        user.setPassword(pwd);
        user.login(LoginActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                Snackbar.make(mLoginFromView,"登陆成功",Snackbar.LENGTH_SHORT).show();
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra(SplashActivity.SEND_USER_NAME,name);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                showProgress(false);
                Snackbar.make(mLoginFromView,s,Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 用户名必须大于4位
     */
    private boolean isUserNameValid(String userName){

        return userName.length()>=4;
    }
    /**
     * 密码必须大于5位
     */
    private boolean isPasswordValid(String password){

        return password.length()>=5;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private  void showProgress(final boolean show){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB_MR2){
            int shortAnimTime=getResources().getInteger(android.R.integer.config_shortAnimTime);
            mLoginFromView.setVisibility(show?View.GONE:View.VISIBLE);
            mLoginFromView.animate().setDuration(shortAnimTime).alpha(show?0:1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFromView.setVisibility(show?View.GONE:View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show?View.VISIBLE:View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(show?1:0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show?View.VISIBLE:View.GONE);
                }
            });
        }else{
            mProgressView.setVisibility(show?View.VISIBLE:View.GONE);
            mLoginFromView.setVisibility(show?View.GONE:View.VISIBLE);
        }
    }
    /**
     * 点击此按钮时，注册新用户
     */
    public void tv_signup(View view){
        TextView tv=(TextView) findViewById(R.id.id_signup_textview);
        tv.setTextColor(Color.RED);
        Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(intent);
        //finish();
    }

//    @Override
//    public void onBackPressed() {
//        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//        startActivity(intent);
//    }
}
