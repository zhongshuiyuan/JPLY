package com.titan.ycslzy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.titan.baselibrary.util.ToastUtil;
import com.titan.ycslzy.MyApplication;
import com.titan.ycslzy.R;
import com.titan.ycslzy.service.RetrofitHelper;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by li on 2016/5/26.
 * 启动开始页面，系统选择
 */
public class LoginActivity extends Activity implements OnClickListener {

    private Context mContext;
    public static boolean isLogin = false;
    private EditText loginName,loginPassword;
    private TextView loginCheckBtn;
    private CheckBox loginCbx;
    private String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;

        initView();
		/*初始化数据*/
        //initData();
    }

    /**初始化控件*/
    private void initView(){
        loginName =(EditText) findViewById(R.id.login_usetname);
        String name = MyApplication.sharedPreferences.getString("username","");
        loginName.setText(name);
        loginPassword =(EditText) findViewById(R.id.login_password);
        String password = MyApplication.sharedPreferences.getString("password","");
        loginPassword.setText(password);
        loginCheckBtn =(TextView) findViewById(R.id.login_check_suss);
        loginCheckBtn.setOnClickListener(this);
        loginCbx =(CheckBox) findViewById(R.id.login_cbx);
        boolean flag = MyApplication.sharedPreferences.getBoolean("cbxstate",true);
        loginCbx.setChecked(flag);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_check_suss:
                loginCheck();
                break;
        }
    }

    /**登录检查用户名和密码*/
    private void loginCheck(){
        username = loginName.getText().toString().trim();
        password = loginPassword.getText().toString().trim();
        Observable<String> oberver = RetrofitHelper.getInstance(this).getServer().checkUser(username,password);
        oberver.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtil.setToast(mContext,"数据连接错误");
                    }

                    @Override
                    public void onNext(String s) {
                        if(!s.equals("")){
                            String[] arrays = s.split(",");
                            if(arrays[0].equals("0")){
                                ToastUtil.setToast(mContext,arrays[1]);
                            }else if(arrays[0].equals("1")){
                                ToastUtil.setToast(mContext,arrays[1]);
                                toMainActivity();
                            }else if(arrays[0].equals("0")){
                                ToastUtil.setToast(mContext,arrays[1]);
                            }
                        }
                    }
                });
    }

    private void toMainActivity(){
        Intent intent = new Intent(LoginActivity.this,YzlActivity.class);
        startActivity(intent);

        if(loginCbx.isChecked()){
            MyApplication.sharedPreferences.edit().putString("username",username).apply();
            MyApplication.sharedPreferences.edit().putString("password",password).apply();
            MyApplication.sharedPreferences.edit().putBoolean("cbxstate",true).apply();
        }
    }
}
