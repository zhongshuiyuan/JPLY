package com.titan.ycslzy.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lling.photopicker.PhotoPickerActivity;
import com.titan.ycslzy.BaseActivity;
import com.titan.ycslzy.MyApplication;
import com.titan.ycslzy.R;
import com.titan.ycslzy.activity.ImageBrowseActivity;
import com.titan.ycslzy.adapter.RecyclerViewPicAdapter;
import com.titan.ycslzy.entity.Image;
import com.titan.ycslzy.entity.SceneInfo;
import com.titan.ycslzy.mview.IBaseView;
import com.titan.ycslzy.mview.IDialog;
import com.titan.ycslzy.service.RetrofitHelper;
import com.titan.ycslzy.util.ToastUtil;
import com.titan.ycslzy.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 现场信息采集
 * Created by li on 2017/10/18.
 */

public class SceneInfoDialog extends Dialog implements IDialog{

    private BaseActivity mContext;
    private IBaseView baseView;
    private TextView scenezp;
    private RecyclerView recyclerViewPic;
    private EditText sjmc,sjms,lon,lat,remark;
    private ArrayList<String> picList = new ArrayList<>();

    public SceneInfoDialog(@NonNull BaseActivity context, @StyleRes int themeResId, IBaseView baseView) {
        super(context, themeResId);
        this.mContext = context;
        this.baseView = baseView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_sceneinfo);
        setCanceledOnTouchOutside(false);

        initView();
    }

    private void initView(){
        sjmc =(EditText) findViewById(R.id.scene_sjmc);
        sjms =(EditText) findViewById(R.id.scene_sjms);
        lon =(EditText) findViewById(R.id.scene_lon);
        String lonstr = baseView.getCurrentLon()+"";
        lon.setText(lonstr);
        lat =(EditText) findViewById(R.id.scene_lat);
        String latstr = baseView.getCurrenLat()+"";
        lat.setText(latstr);
        remark =(EditText) findViewById(R.id.scene_bz);
        scenezp =(TextView) findViewById(R.id.scene_zp);
        scenezp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSelectPic();
            }
        });
        recyclerViewPic =(RecyclerView) findViewById(R.id.scene_pic);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerViewPic.setLayoutManager(manager);


        TextView save =(TextView) findViewById(R.id.scene_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = sjmc.getText().toString().trim();
                ArrayList<Image> images = new ArrayList<>();
                for(String path : picList){
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    String base = Util.Bitmap2StrByBase64(bitmap);
                    images.add(new Image(path,base));
                }
                Gson gson = new Gson();
                String pic = gson.toJson(images);
                String xxmc = sjms.getText().toString().trim();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time =dateFormat.format(new Date());
                String lonstr = lon.getText().toString().trim();
                String latstr = lat.getText().toString().trim();
                String bz = remark.getText().toString().trim();
                SceneInfo sceneInfo = new SceneInfo(name,MyApplication.macAddress,pic,xxmc,time,lonstr,latstr,bz);
                String json = gson.toJson(sceneInfo);
                addSceneInfo(json);
            }
        });

        ImageView close =(ImageView) findViewById(R.id.scene_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 跳转到选择图片界面
     */
    public void toSelectPic() {
        if (mContext.picList.size() == 9) {
            ToastUtil.setToast(mContext, mContext.getResources().getString(R.string.log_pic_select_tip));
            return;
        }
        Intent intent = new Intent(mContext, PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);//是否显示相机
        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);//选择模式（默认多选模式）
        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);//最大照片张数
        mContext.startActivityForResult(intent, mContext.PICK_PHOTO);
    }

    private void addSceneInfo(String json){
        Observable<String> observable = RetrofitHelper.getInstance(mContext).getServer().addXjsjInfo(json);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.setToast(mContext,e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        if(!s.equals("0")){
                            dismiss();
                            ToastUtil.setToast(mContext,"成功");
                        }else{
                            ToastUtil.setToast(mContext,"失败");
                        }
                    }
                });
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerViewPic;
    }


    /**选择图片后加载图片*/
    public void loadPhoto(SceneInfoDialog sceneInfoDialog, List<String> list, int mColumnWidth, RecyclerViewPicAdapter.PicOnclick picOnclick){
        picList.addAll(list);
        RecyclerView recyclerView = sceneInfoDialog.getRecyclerView();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerViewPicAdapter adapterImg = new RecyclerViewPicAdapter(mContext,list,mColumnWidth);
        if(adapterImg != null){
            recyclerView.setAdapter(adapterImg);
            adapterImg.setPicOnclick(picOnclick);
        }
    }

}
