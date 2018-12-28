package com.otitan.gylyeq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.otitan.gylyeq.BaseActivity;
import com.otitan.gylyeq.MyApplication;
import com.otitan.gylyeq.R;
import com.otitan.gylyeq.mview.IBaseView;
import com.otitan.gylyeq.presenter.GpsCollectPresenter;
import com.otitan.gylyeq.util.BussUtil;
import com.otitan.gylyeq.util.ToastUtil;
import com.titan.baselibrary.listener.CancleListener;
import com.titan.versionupdata.VersionUpdata;

/**
 * Created by li on 2017/5/31.
 * 系统设置dialog
 */

public class SettingDialog extends Dialog {

    private Context mContext;
    private View gpsCaijiInclude;
    private IBaseView iBaseView;
    private GpsCollectPresenter gpsCollectPresenter;

    public SettingDialog(@NonNull Context context, @StyleRes int themeResId,IBaseView baseView,GpsCollectPresenter gpsCollectPresenter) {
        super(context, themeResId);
        this.mContext = context;
        this.iBaseView = baseView;
        this.gpsCaijiInclude = iBaseView.getGpsCaijiInclude();
        this.gpsCollectPresenter = gpsCollectPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_settings);
        setCanceledOnTouchOutside(false);

        CheckBox xsxjlx = (CheckBox) findViewById(R.id.xsxjlx);
        xsxjlx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean check) {
                gpsCollectPresenter.setTravel(check);
                MyApplication.sharedPreferences.edit().putBoolean("zongji", check).apply();
            }
        });

        TextView gpsset = (TextView) findViewById(R.id.gpsset);
        gpsset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                GpsSetDialog setDialog = new GpsSetDialog(mContext,R.style.Dialog);
                BussUtil.setDialogParams(mContext, setDialog, 0.5, 0.5);
            }
        });

        TextView version = (TextView) findViewById(R.id.version_check);
        double code = new VersionUpdata((BaseActivity)mContext).getVersionCode(mContext);
        version.setText("版本更新   "+code);
        version.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                new UpdataThread().start();
            }
        });

        final CheckBox gpsCjlx = (CheckBox) findViewById(R.id.lxcj);
        if(gpsCaijiInclude.getVisibility() == View.VISIBLE){
            gpsCjlx.setChecked(true);
        }
        gpsCjlx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if(arg1){
                    gpsCollectPresenter.showCollectionType(gpsCaijiInclude,SettingDialog.this);
                }else{
                    gpsCaijiInclude.setVisibility(View.GONE);
                }
            }
        });

        ImageView close = (ImageView) findViewById(R.id.settings_close);
        close.setOnClickListener(new CancleListener(this));


        TextView usersetting =(TextView) findViewById(R.id.registeruserinfo);
        usersetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobileInputDialog dialog = new MobileInputDialog(mContext,R.style.Dialog);
                BussUtil.setDialogParams(mContext, dialog, 0.5, 0.8);
            }
        });
    }

    /*检查版本更新*/
    private class UpdataThread extends Thread{

        @Override
        public void run() {
            super.run();
            if(MyApplication.getInstance().netWorkTip()){
                // 获取当前版本号 是否是最新版本
                String updateurl = mContext.getResources().getString(R.string.updateurl);
                boolean flag = new VersionUpdata((BaseActivity)mContext).checkVersion(updateurl);
                if(!flag){
                    ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.setToast(mContext, "已是最新版本");
                        }
                    });
                }
            }
        }
    }

}
