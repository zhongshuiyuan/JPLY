package com.titan.ycslzy.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.lling.photopicker.PhotoPickerActivity;
import com.titan.baselibrary.listener.CancleListener;
import com.titan.baselibrary.util.ToastUtil;
import com.titan.ycslzy.BaseActivity;
import com.titan.ycslzy.MyApplication;
import com.titan.ycslzy.R;
import com.titan.ycslzy.activity.ImageBrowseActivity;
import com.titan.ycslzy.adapter.Recyc_imageAdapter;

import java.util.ArrayList;

/**
 * Created by li on 2017/7/6.
 * 紧急信息上报
 */

public class JjxxsbDialog extends Dialog implements Recyc_imageAdapter.PicOnclick {

    private static BaseActivity mContext;
    private static JjxxsbDialog dialog;
    public static ArrayList<String> picList = new ArrayList<>();
    private static RecyclerView recyc;
    private static int width = 0;

    public JjxxsbDialog(@NonNull BaseActivity context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        width = this.getWindow().getAttributes().width;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_jjxxsb);
        setCanceledOnTouchOutside(false);
        dialog = this;
        initview();

    }

    /*初始化控件*/
    private void initview() {
        ImageView close = (ImageView) findViewById(R.id.xxsb_close);
        close.setOnClickListener(new CancleListener(this));

        Spinner sjlx = (Spinner) findViewById(R.id.sjlx_spinner);
        sjlx.setSelection(0, false);
        sjlx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        EditText sjms = (EditText) findViewById(R.id.txt_sjms);
        EditText fsdd = (EditText) findViewById(R.id.txt_fsdd);
        EditText tel = (EditText) findViewById(R.id.txt_tel);
        EditText beizhu = (EditText) findViewById(R.id.txt_beizhu);
        TextView pic =(TextView) findViewById(R.id.xczp_pic);
        recyc = (RecyclerView) findViewById(R.id.txt_xczp);

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSelectPic();
            }
        });

        //loadPhoto();

        TextView sure = (TextView) findViewById(R.id.xxsb_save);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getInstance().netWorkTip()) {
                    //网络连接后上报成功
                    ToastUtil.setToast(mContext, "上报成功");
                    dismiss();
                }
            }
        });

    }

    /*发送数据*/
    private void senInofToServer() {

    }

    @Override
    public void setPicOnclick(View item, int position) {
        Intent intent = new Intent(mContext,ImageBrowseActivity.class);
        intent.putStringArrayListExtra("images",picList);
        intent.putExtra("position",position);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到选择图片界面
     */
    public void toSelectPic() {
        if (picList.size() != 0 && picList.size() != 3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("重新选择会覆盖之前的图片");
            builder.setMessage("是否重新选择");
            builder.setCancelable(true);
            builder.setPositiveButton("重新选择", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(mContext, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);//是否显示相机
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);//选择模式（默认多选模式）
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);//最大照片张数
                    mContext.startActivityForResult(intent, mContext.PICK_PHOTO);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(16);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(16);
        }
        if (picList.size() == 3) {
            ToastUtil.setToast(mContext, "照片最多只能选择9张");
            return;
        }
        if (picList.size() == 0) {
            Intent intent = new Intent(mContext, PhotoPickerActivity.class);
            intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);//是否显示相机
            intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);//选择模式（默认多选模式）
            intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);//最大照片张数
            mContext.startActivityForResult(intent, mContext.PICK_PHOTO);
        }
    }

    /**选择图片后加载图片*/
    public static void loadPhoto(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyc.setLayoutManager(layoutManager);
        Recyc_imageAdapter adapter = new Recyc_imageAdapter(mContext, picList, SettingDialog.dialogParams.width/4);
        if(adapter != null){
            recyc.setAdapter(adapter);
            adapter.setPicOnclick(dialog);
        }
    }
}

