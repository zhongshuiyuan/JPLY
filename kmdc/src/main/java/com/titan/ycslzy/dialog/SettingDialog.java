package com.titan.ycslzy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.titan.ycslzy.BaseActivity;
import com.titan.ycslzy.MyApplication;
import com.titan.ycslzy.R;
import com.titan.ycslzy.mview.IBaseView;
import com.titan.ycslzy.presenter.GpsCollectPresenter;
import com.titan.ycslzy.util.BussUtil;
import com.titan.ycslzy.util.ToastUtil;
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


    public SettingDialog(@NonNull Context context, @StyleRes int themeResId,IBaseView baseView) {
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
            public void onCheckedChanged(CompoundButton arg0, boolean check) {
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
        version.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(MyApplication.getInstance().netWorkTip()){
                    // 获取当前版本号 是否是最新版本
                    String updataurl = mContext.getResources().getString(R.string.updateurl);
                    boolean flag = new VersionUpdata((BaseActivity)mContext).checkVersion(updataurl);
                    if (!flag) {
                        ToastUtil.setToast(mContext, "已是最新版本");
                    }
                }
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
                    gpsCollectPresenter.showCollectionType();
                    gpsCaijiInclude.setVisibility(View.VISIBLE);
                }else{
                    gpsCaijiInclude.setVisibility(View.GONE);
                }
            }
        });

        ImageView close = (ImageView) findViewById(R.id.settings_close);
        close.setOnClickListener(new CancleListener(this));

    }
}
