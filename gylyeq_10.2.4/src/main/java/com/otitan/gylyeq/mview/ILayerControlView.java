package com.otitan.gylyeq.mview;

import android.content.SharedPreferences;
import android.view.View;

import com.esri.android.map.MapView;
import com.esri.android.map.TiledLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.otitan.gylyeq.entity.MyLayer;
import com.otitan.gylyeq.entity.Row;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by li on 2017/5/5.
 *
 * 图层控制接口
 */

public interface ILayerControlView extends IBaseView{

    View getParChildView();

    void addImageLayer(File file);

    void addDxtLayer(File file);

    List<MyLayer> getLayerNameList();

    View getImgeLayerView();

    View getDxtLayerView();

    View getTckzView();

    HashMap<String, Boolean> getLayerCheckBox();

    List<Row> getSysLayerData();

    List<String> getLayerKeyList();

    SharedPreferences getSharedPreferences();
}
