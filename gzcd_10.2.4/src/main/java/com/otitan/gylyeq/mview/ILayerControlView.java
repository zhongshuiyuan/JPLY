package com.otitan.gylyeq.mview;

import android.content.SharedPreferences;
import android.view.View;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.otitan.gylyeq.entity.MyLayer;
import com.otitan.gylyeq.entity.Row;

import java.util.HashMap;
import java.util.List;

/**
 * Created by li on 2017/5/5.
 *
 * 图层控制接口
 */

public interface ILayerControlView extends IBaseView{

    View getParChildView();

    void addImageLayer(String path);

    List<MyLayer> getLayerNameList();

    View getImgeLayerView();

    View getTckzView();

    HashMap<String, Boolean> getLayerCheckBox();

    List<Row> getSysLayerData();

    List<String> getLayerKeyList();

    SharedPreferences getSharedPreferences();
}
