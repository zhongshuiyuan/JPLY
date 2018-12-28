package com.titan.ycslzy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.titan.baselibrary.listener.CancleListener;
import com.titan.baselibrary.util.ToastUtil;
import com.titan.ycslzy.R;
import com.titan.ycslzy.mview.IBaseView;
import com.titan.ycslzy.util.SpatiaProUtil;

/**
 * Created by li on 2017/6/1.
 * 坐标定位
 */

public class CoordinateDialog extends Dialog {

    private Context mContext;
    private IBaseView iBaseView;
    private double x, y;

    public CoordinateDialog(@NonNull Context context, @StyleRes int themeResId, IBaseView baseView) {
        super(context, themeResId);
        this.mContext = context;
        this.iBaseView = baseView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_input);

        final RadioButton radio_reft = (RadioButton) findViewById(R.id.radio_btn_left);
        radio_reft.setChecked(true);
        final RadioButton radio_center = (RadioButton) findViewById(R.id.radio_btn_center);
        final RadioButton radio_right = (RadioButton) findViewById(R.id.radio_btn_right);
        final LinearLayout layout_type01 = (LinearLayout) findViewById(R.id.layout_type01);
        final LinearLayout layout_type02 = (LinearLayout) findViewById(R.id.layout_type02);
        final LinearLayout layout_type03 = (LinearLayout) findViewById(R.id.layout_type03);
        radio_reft.setTextColor(mContext.getResources().getColor(R.color.blue));
        layout_type01.setVisibility(View.VISIBLE);
        layout_type02.setVisibility(View.GONE);
        layout_type03.setVisibility(View.GONE);
        radio_reft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    radio_reft.setTextColor(mContext.getResources().getColor(R.color.blue));
                    radio_center.setTextColor(mContext.getResources().getColor(R.color.balck));
                    radio_right.setTextColor(mContext.getResources().getColor(R.color.balck));
                    layout_type01.setVisibility(View.VISIBLE);
                    layout_type02.setVisibility(View.GONE);
                    layout_type03.setVisibility(View.GONE);
                }

            }
        });
        radio_center.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    radio_reft.setTextColor(mContext.getResources().getColor(R.color.balck));
                    radio_center.setTextColor(mContext.getResources().getColor(R.color.blue));
                    radio_right.setTextColor(mContext.getResources().getColor(R.color.balck));
                    layout_type01.setVisibility(View.GONE);
                    layout_type02.setVisibility(View.VISIBLE);
                    layout_type03.setVisibility(View.GONE);
                }
            }
        });

        radio_right.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean arg1) {
                if (arg1) {
                    radio_reft.setTextColor(mContext.getResources().getColor(R.color.balck));
                    radio_center.setTextColor(mContext.getResources().getColor(R.color.balck));
                    radio_right.setTextColor(mContext.getResources().getColor(R.color.blue));
                    layout_type01.setVisibility(View.GONE);
                    layout_type02.setVisibility(View.GONE);
                    layout_type03.setVisibility(View.VISIBLE);
                }
            }
        });
        final EditText edit_jd = (EditText) findViewById(R.id.edit_jd);
        final EditText edit_wd = (EditText) findViewById(R.id.edit_wd);
        final EditText edit_jd_d = (EditText) findViewById(R.id.edit_jd_d);
        final EditText edit_jd_f = (EditText) findViewById(R.id.edit_jd_f);
        final EditText edit_jd_m = (EditText) findViewById(R.id.edit_jd_m);
        final EditText edit_wd_d = (EditText) findViewById(R.id.edit_wd_d);
        final EditText edit_wd_f = (EditText) findViewById(R.id.edit_wd_f);
        final EditText edit_wd_m = (EditText) findViewById(R.id.edit_wd_m);
        // 米制x
        final EditText edit_x = (EditText) findViewById(R.id.edit_x);
        // 米制y
        final EditText edit_y = (EditText) findViewById(R.id.edit_y);

        TextView lon = (TextView) findViewById(R.id.location_lon);
        String lontxt = iBaseView.getCurrentLon() + "";
        lon.setText(lontxt);

        TextView lat = (TextView) findViewById(R.id.location_lat);
        String lattxt = iBaseView.getCurrenLat() + "";
        lat.setText(lattxt);

        // 确定按钮
        Button btn_confirm = (Button) findViewById(R.id.btn_confirm);

        btn_confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //
                Point point = null;
                if (radio_reft.isChecked()) {
                    // 经纬度格式
                    if (TextUtils.isEmpty(edit_jd.getText().toString())) {
                        ToastUtil.setToast(mContext, "请输入对应数据");
                        return;
                    }
                    if (TextUtils.isEmpty(edit_wd.getText().toString())) {
                        ToastUtil.setToast(mContext, "请输入对应数据");
                        return;
                    }
                    //
                    x = Double.valueOf(edit_jd.getText().toString().trim());
                    y = Double.valueOf(edit_wd.getText().toString().trim());

                    // 判断经纬度是否在中国境内
                    if (x > 135 || x < 74 || y > 54 || y < 3) {
                        ToastUtil.setToast(mContext, "坐标范围不在中国内,请重新输入");
                        return;
                    }

                } else if (radio_center.isChecked()) {
                    // 度分秒格式
                    if (TextUtils.isEmpty(edit_jd_d.getText().toString())) {
                        ToastUtil.setToast(mContext, "请输入对应数据");
                        return;
                    }
                    if (TextUtils.isEmpty(edit_jd_f.getText().toString())) {
                        ToastUtil.setToast(mContext, "请输入对应数据");
                        return;
                    }
                    if (TextUtils.isEmpty(edit_jd_m.getText().toString())) {
                        ToastUtil.setToast(mContext, "请输入对应数据");
                        return;
                    }
                    if (TextUtils.isEmpty(edit_wd_d.getText().toString())) {
                        ToastUtil.setToast(mContext, "请输入对应数据");
                        return;
                    }
                    if (TextUtils.isEmpty(edit_wd_f.getText().toString())) {
                        ToastUtil.setToast(mContext, "请输入对应数据");
                        return;
                    }
                    if (TextUtils.isEmpty(edit_wd_m.getText().toString())) {
                        ToastUtil.setToast(mContext, "请输入对应数据");
                        return;
                    }
                    //
                    x = Integer.valueOf(edit_jd_d.getText().toString().trim())
                            + (double) Integer.valueOf(edit_jd_f.getText()
                            .toString().trim())
                            / 60
                            + (double) Integer.valueOf(edit_jd_m.getText()
                            .toString().trim()) / 3600;
                    y = Integer.valueOf(edit_wd_d.getText().toString().trim())
                            + (double) Integer.valueOf(edit_wd_f.getText()
                            .toString().trim())
                            / 60
                            + (double) Integer.valueOf(edit_wd_m.getText()
                            .toString().trim()) / 3600;

                    // 判断经纬度是否在中国境内
                    if (x > 135 || x < 74 || y > 54 || y < 3) {
                        ToastUtil.setToast(mContext, "坐标范围不在中国内,请重新输入");
                        return;
                    }

                } else if (radio_right.isChecked()) {
                    if (TextUtils.isEmpty(edit_x.getText().toString())) {
                        ToastUtil.setToast(mContext, "请输入对应数据");
                        return;
                    }
                    if (TextUtils.isEmpty(edit_y.getText().toString())) {
                        ToastUtil.setToast(mContext, "请输入对应数据");
                        return;
                    }
                    // 还有问题
                    x = Double.parseDouble(edit_x.getText().toString());
                    y = Double.parseDouble(edit_y.getText().toString());
                }

                if (radio_reft.isChecked()) {
                    //point = SymbolUtil.getPoint(x, y);
                    point = new Point(x,y);
                    if(!iBaseView.getSpatialReference().isWGS84()){
                        point =(Point) GeometryEngine.project(point, SpatiaProUtil.wgs_4326(),iBaseView.getSpatialReference());
                    }
                } else if (radio_center.isChecked()) {
                    //point = SymbolUtil.getPoint(x, y);
                    point = new Point(x,y);
                    if(!iBaseView.getSpatialReference().isWGS84()){
                        point =(Point) GeometryEngine.project(point, SpatiaProUtil.wgs_4326(),iBaseView.getSpatialReference());
                    }
                } else if (radio_right.isChecked()) {
                    point = new Point(x, y);
                    if(iBaseView.getSpatialReference().isWGS84()){
                        point =(Point) GeometryEngine.project(point, SpatiaProUtil.xian_2382(),iBaseView.getSpatialReference());
                    }
                }

                iBaseView.getGraphicLayer().removeAll();
                PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mContext.getResources().getDrawable(R.drawable.icon_gcoding));
                pictureMarkerSymbol.setOffsetY(20);
                Graphic graphic = new Graphic(point, pictureMarkerSymbol);
                iBaseView.getGraphicLayer().addGraphic(graphic);
                iBaseView.getMapView().setExtent(point, 0, true);
                dismiss();
            }
        });

        ImageView text_close = (ImageView) findViewById(R.id.text_close);
        text_close.setOnClickListener(new CancleListener(this));
    }
}
