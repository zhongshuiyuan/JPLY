package com.titan.ycslzy.mview;
import com.esri.android.map.TiledLayer;
import com.titan.ycslzy.drawTool.DrawTool;
import com.titan.ycslzy.entity.MyLayer;

import java.util.List;

/**
 * Created by li on 2017/6/1.
 * 加载图层接口
 */

public interface ILayerView extends IBaseView {

    //获取电子底图
    TiledLayer getBaseTitleLayer();
    //获取影像底图
    TiledLayer getImgTitleLayer();
    //获取地形图底图
    TiledLayer getDxtTitleLayer();
    //获取加载的小班图层列表
    List<MyLayer> getLayerList();
    //获取DrawTool
    DrawTool getDrawTool();


}
