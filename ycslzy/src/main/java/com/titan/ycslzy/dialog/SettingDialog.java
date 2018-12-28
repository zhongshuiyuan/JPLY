package com.titan.ycslzy.dialog;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.titan.baselibrary.listener.CancleListener;
import com.titan.baselibrary.util.ToastUtil;
import com.titan.versionupdata.VersionUpdata;
import com.titan.ycslzy.BaseActivity;
import com.titan.ycslzy.MyApplication;
import com.titan.ycslzy.R;
import com.titan.ycslzy.mview.IBaseView;
import com.titan.ycslzy.presenter.GpsCollectPresenter;
import com.titan.ycslzy.util.BussUtil;

/**
 * Created by li on 2017/5/31.
 * 系统设置dialog
 */

public class SettingDialog extends Dialog {

    private BaseActivity mContext;
    private View gpsCaijiInclude;
    private IBaseView iBaseView;
    public static WindowManager.LayoutParams dialogParams;


    public SettingDialog(@NonNull BaseActivity context, @StyleRes int themeResId, IBaseView baseView) {
        super(context, themeResId);
        this.mContext = context;
        this.iBaseView = baseView;
        this.gpsCaijiInclude = iBaseView.getGpsCaijiInclude();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_settings);
        setCanceledOnTouchOutside(false);

        CheckBox xsxjlx = (CheckBox) findViewById(R.id.xsxjlx);
        xsxjlx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyApplication.sharedPreferences.edit().putBoolean("zongji", isChecked).apply();
            }
        });

        CheckBox gps =(CheckBox) findViewById(R.id.gpskaiguan);
        gps.setChecked(BussUtil.isOPen(mContext));
        gps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    int sdk_int = Build.VERSION.SDK_INT;
                    int kitkat = Build.VERSION_CODES.KITKAT;
                    if(sdk_int < 14){
                        opengps(mContext);
                    }else{
                        Intent intent =  new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                }
            }
    });

        TextView gpsset = (TextView) findViewById(R.id.gpsset);
        gpsset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                GpsSetDialog setDialog = new GpsSetDialog(mContext,R.style.Dialog);
                BussUtil.setDialogParams(mContext, setDialog, 0.5, 0.7);
            }
        });

        TextView version = (TextView) findViewById(R.id.version_check);
        double code = new VersionUpdata((BaseActivity)mContext).getVersionCode(mContext);
        version.setText("版本更新   "+code);
        version.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                new UpdataThread(mContext).start();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(MyApplication.getInstance().netWorkTip()){
//                            // 获取当前版本号 是否是最新版本
//                            String updataurl = mContext.getResources().getString(R.string.updateurl);
//                            boolean flag = new VersionUpdata((BaseActivity)mContext).checkVersion(updataurl);
//                            if (!flag) {
//                                ToastUtil.setToast(mContext, "已是最新版本");
//                            }
//                        }
//                    }
//                }).start();
            }
        });

        final CheckBox gpsCjlx = (CheckBox) findViewById(R.id.lxcj);
        if(gpsCaijiInclude.getVisibility() == View.VISIBLE){
            gpsCjlx.setChecked(true);
        }
        final GpsCollectPresenter gpsCollectPresenter = new GpsCollectPresenter(mContext,iBaseView);
        gpsCjlx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if(arg1){
                    gpsCollectPresenter.showCollectionType(gpsCaijiInclude,SettingDialog.this);
                    gpsCaijiInclude.setVisibility(View.VISIBLE);
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

        TextView xcpz =(TextView) findViewById(R.id.pop_recordepic);
        xcpz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JjxxsbDialog jjxxsbDialog = new JjxxsbDialog(mContext,R.style.Dialog);
                dialogParams = BussUtil.setDialogParams(mContext, jjxxsbDialog, 0.5, 0.65);
            }
        });
    }

    private void opengps(Context context){
        Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings","com.android.settings.widget.SettingsAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
        }
        catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /*检查版本更新*/
    public static class UpdataThread extends Thread{
        private Context context;
        public UpdataThread(Context context){
            this.context = context;
        }

        @Override
        public void run() {
            super.run();
            if(MyApplication.getInstance().netWorkTip()){
                // 获取当前版本号 是否是最新版本
                String updateurl = context.getResources().getString(R.string.updateurl);
                boolean flag = new VersionUpdata((BaseActivity)context).checkVersion(updateurl);
                if(flag){
                    //提示更新

                }else{
                    //已是最新版本
                    if (!flag) {
                        ToastUtil.setToast(context, "已是最新版本");
                    }
                }
            }
        }
    }
}
