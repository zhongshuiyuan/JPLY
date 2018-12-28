package com.otitan.gylyeq.mview;

import android.view.View;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.TiledLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by li on 2017/5/9.
 * base基类接口
 */

public interface IBaseView {

    //获取电子底图
    TiledLayer getBaseTitleLayer();
    //获取影像底图
    HashMap<File,TiledLayer> getImgTitleLayer();
    //获取地形图底图
    HashMap<File,TiledLayer> getDxtTitleLayer();

    Callout getCallout();

    double getCurrentLon();

    double getCurrenLat();

    Point getMapPoint();

    Point getGpsPoint();

    GraphicsLayer getGraphicLayer();

    void removeGraphicLayer();

    void addGraphicLayer();

    SpatialReference getSpatialReference();

    View getGpsCaijiInclude();

    MapView getMapView();

    Envelope getCurrentEnvelope();

}
