package com.otitan.gylyeq.presenter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.TiledLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.renderer.UniqueValue;
import com.esri.core.renderer.UniqueValueRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.otitan.gylyeq.BaseActivity;
import com.otitan.gylyeq.MyApplication;
import com.otitan.gylyeq.R;
import com.otitan.gylyeq.adapter.DxtTucengAdapter;
import com.otitan.gylyeq.adapter.ExpandableAdapter;
import com.otitan.gylyeq.adapter.ImgTucengAdapter;
import com.otitan.gylyeq.dialog.AddLayerDialog;
import com.otitan.gylyeq.entity.MyLayer;
import com.otitan.gylyeq.entity.ScreenTool;
import com.otitan.gylyeq.mview.ILayerControlView;
import com.otitan.gylyeq.util.BaseUtil;
import com.otitan.gylyeq.util.BussUtil;
import com.otitan.gylyeq.util.SpatialUtil;
import com.otitan.gylyeq.util.SytemUtil;
import com.otitan.gylyeq.util.ToastUtil;
import com.otitan.gylyeq.util.Util;
import com.titan.baselibrary.util.ProgressDialogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by li on 2017/5/5.
 * 图层控制Presenter
 */

public class LayerControlPresenter {

    private Context mContext;
    private ILayerControlView controlView;
    /** 影像图文件地址 */
    public HashMap<String, Boolean> imgCheckMap = new HashMap<String, Boolean>();
    /** 地形图文件地址 */
    public HashMap<String, Boolean> dxtCheckMap = new HashMap<String, Boolean>();

    private View childView;
    private View.OnClickListener onClickListener;

    public LayerControlPresenter(Context ctx, ILayerControlView view, View.OnClickListener onClickListener){
        this.mContext = ctx;
        this.controlView = view;
        this.onClickListener = onClickListener;
        childView = controlView.getParChildView();
    }


    /** 图层控制加载otms数据 */
    public void initOtmsData() {
        // 基础图
        CheckBox cb_sl = (CheckBox) childView.findViewById(R.id.cb_sl);
        if (controlView.getBaseTitleLayer() != null) {
            cb_sl.setChecked(controlView.getBaseTitleLayer().isVisible());
        }
        // 基础图
        cb_sl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, final boolean flag) {
                if(controlView.getBaseTitleLayer() != null){
                    controlView.getBaseTitleLayer().setVisible(flag);
                }
            }
        });
        // 基础图 缩放到地图范围
        ImageView tileView = (ImageView)childView.findViewById(R.id.tile_extent);
        tileView.setOnClickListener(onClickListener);

        //地形图
        final CheckBox cb_dxt = (CheckBox) childView.findViewById(R.id.cb_dxt);
        if (controlView.getDxtTitleLayer().size()> 0) {
            cb_dxt.setChecked(true);
        }
        // 地形图
        cb_dxt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, final boolean flag) {
                dxtLayerChange(flag,cb_dxt);
            }
        });
        // 地形图 缩放到地图范围
        ImageView dxtTileView = (ImageView) childView.findViewById(R.id.dxt_extent);
        dxtTileView.setOnClickListener(onClickListener);

        // 影像图
        final CheckBox cb_yx = (CheckBox) childView.findViewById(R.id.cb_ys);
        if (BussUtil.objEmperty(controlView.getImgTitleLayer())) {
            cb_yx.setChecked(false);
        }
        // 影像
        cb_yx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                imgLayerChange(arg0,arg1);
            }
        });
        // 影像图所放到地图范围
        ImageView imageView = (ImageView) childView.findViewById(R.id.image_extent);
        imageView.setOnClickListener(onClickListener);

        /*加载小班矢量数据*/
        initOtmsData("");

        ImageView closeView = (ImageView) childView.findViewById(R.id.close_tuceng);
        closeView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                controlView.getTckzView().setVisibility(View.GONE);
            }
        });
    }

    /*地形图图层选择*/
    private void dxtLayerChange(boolean flag,CheckBox dxtcheckbox){

        if(!flag){
            dxtCheckMap.clear();
            controlView.getDxtLayerView().setVisibility(View.GONE);
            HashMap<File,TiledLayer> layers = controlView.getDxtTitleLayer();
            if(layers.size() == 0){
                return;
            }
            for(File file : layers.keySet()){
                controlView.getMapView().removeLayer(layers.get(file));
            }
            controlView.getDxtTitleLayer().clear();
            controlView.getMapView().invalidate();
            return;
        }
        List<File> fileList = MyApplication.resourcesManager.getDxtFiles();
        int size = fileList.size();
        if(size == 1){
            File file = fileList.get(0);
            controlView.addDxtLayer(file);
        }else if(size == 0){
            String path = MyApplication.resourcesManager.getDxtLayerPath();
            if(!new File(path).exists()){
                ToastUtil.setToast(mContext,"没有地形数据");
                return;
            }
            controlView.addDxtLayer(new File(path));
        }else{
            showDxtLayerSelect(dxtcheckbox,fileList);
        }

    }

    /*影像图层选择变化*/
    private void imgLayerChange(CompoundButton checkBox,boolean flag){
        if(!flag){
            imgCheckMap.clear();
            controlView.getImgeLayerView().setVisibility(View.GONE);
            HashMap<File,TiledLayer> layers = controlView.getImgTitleLayer();
            if(layers.size() ==0){
                return;
            }
            for(File file : layers.keySet()){
                controlView.getMapView().removeLayer(layers.get(file));
            }
            controlView.getImgTitleLayer().clear();
            controlView.getMapView().invalidate();
            return;
        }
        List<File> fileList = MyApplication.resourcesManager.getImgFiles();
        int size = fileList.size();
        if(size == 0){
            List<File> list= MyApplication.resourcesManager.getImgTitlePath();
            showImgLayerSelect(checkBox,list);
            return;
        }

        if(size == 1){
            File file = fileList.get(0);
            controlView.addImageLayer(file);
            return;
        }

        showImgLayerSelect(checkBox,fileList);


//        if (flag) {
//            if (fileList.size() == 1) {
//                File file = new File(fileList.get(0).getPath());
//                if(!file.exists()){
//                    ToastUtil.setToast(mContext, "影像数据文件不存在");
//                    return;
//                }
//                //移除地形图
//                removeDxtLayer();
//                //移除GraphicLayer图层
//                controlView.removeGraphicLayer();
//                //移除矢量图层
//                removeVectorLayer();
//                //添加影像图
//                controlView.addImageLayer(fileList.get(0).getPath());
//                //添加地形图
//                addDxtLayer();
//                //添加矢量图层
//                addVectorLayer();
//                //添加GraphicLayer图层
//                controlView.addGraphicLayer();
//
//            } else {
//                showImgLayerSelect(fileList, imgTileLayerMap);
//            }
//        } else {
//            if (BussUtil.objEmperty(controlView.getImgTitleLayer())) {
//                if (controlView.getImgTitleLayer().isVisible()) {
//                    controlView.getImgTitleLayer().setVisible(false);
//                }
//            } else {
//                controlView.getImgeLayerView().setVisibility(View.GONE);
//                if (fileList != null) {
//                    for (int i = 0; i < fileList.size(); i++) {
//                        String name = fileList.get(i).getName();
//                        if (imgTileLayerMap.get(name) != null) {
//                            controlView.getMapView().removeLayer(imgTileLayerMap.get(name));
//                            imgTileLayerMap.remove(name);
//                        }
//                        imgCheckMap.put(name,false);
//                    }
//                }
//            }
//        }
    }

    /*移除地形图*/
    private void removeDxtLayer(){
        HashMap<File,TiledLayer> layers = controlView.getDxtTitleLayer();
        if(layers.size() == 0){
            return;
        }
        for(File file : layers.keySet()){
            controlView.getMapView().removeLayer(layers.get(file));
        }
        controlView.getMapView().invalidate();
    }

    /*再添加地形图*/
    private void addDxtLayer(){
        HashMap<File,TiledLayer> layers = controlView.getDxtTitleLayer();
        for(File file : layers.keySet()){
            controlView.getMapView().addLayer(layers.get(file));
        }
        controlView.getMapView().invalidate();
    }

    /*移除矢量数据*/
    private void addVectorLayer(){
        if(controlView.getLayerNameList().size() > 0){
            for(MyLayer myLayer :controlView.getLayerNameList()){
                controlView.getMapView().addLayer(myLayer.getLayer());
            }
        }
        controlView.getMapView().invalidate();
    }
    /*添加矢量数据图层*/
    private void removeVectorLayer(){
        if(controlView.getLayerNameList().size() ==0){
            return;
        }
        for(MyLayer myLayer :controlView.getLayerNameList()){
            controlView.getMapView().removeLayer(myLayer.getLayer());
        }
        controlView.getMapView().invalidate();
    }

    /*添加影像*/
    private void addImgLayer(){
        if(controlView.getImgTitleLayer().size() > 0){
            for(File file : controlView.getImgTitleLayer().keySet()){
                controlView.getMapView().addLayer(controlView.getImgTitleLayer().get(file));
            }
        }
        controlView.getMapView().invalidate();
    }
    /*移除影像*/
    private void removeImgLayer(){
        HashMap<File,TiledLayer> layers = controlView.getImgTitleLayer();
        if(layers.size() ==0){
            return;
        }
        for(File file : layers.keySet()){
            controlView.getMapView().removeLayer(layers.get(file));
        }
        controlView.getMapView().invalidate();
    }

    /*影像图层选择*/
    private void showImgLayerSelect(final CompoundButton checkBox,final List<File> list) {
        controlView.getImgeLayerView().setVisibility(View.VISIBLE);

        initImgCheckBoxData(list);
        final ImgTucengAdapter adapter = new ImgTucengAdapter((BaseActivity) mContext,list,imgCheckMap,controlView.getImgTitleLayer());
        ListView listView = (ListView) controlView.getImgeLayerView().findViewById(R.id.img_tcselector);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                File file = list.get(position);
                boolean isChecked = imgCheckMap.get(list.get(position).getName());
                if (isChecked) {
                    imgCheckMap.put(list.get(position).getName(), false);
                    TiledLayer layerindex = controlView.getImgTitleLayer().get(file);
                    controlView.getMapView().removeLayer(layerindex);
                    controlView.getImgTitleLayer().remove(file);
                } else {
                    imgCheckMap.put(list.get(position).getName(), true);
                    if(!file.exists()){
                        ToastUtil.setToast(mContext, "影像数据文件不存在");
                        return;
                    }
                    //移除地形图
                    removeDxtLayer();
                    //移除GraphicLayer 图层
                    controlView.removeGraphicLayer();
                    //移除矢量图层
                    removeVectorLayer();
                    //添加影像图层
                    controlView.addImageLayer(file);
                    //添加地形图
                    addDxtLayer();
                    //添加矢量数据图层
                    addVectorLayer();
                    //添加GraphicLayer图层
                    controlView.addGraphicLayer();

                }
                adapter.notifyDataSetChanged();
            }
        });

        ImageView imageView = (ImageView) controlView.getImgeLayerView().findViewById(R.id.btselect_info_close);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                controlView.getImgeLayerView().setVisibility(View.GONE);
                if(controlView.getImgTitleLayer().size() == 0){
                    checkBox.setChecked(false);
                }
            }
        });
    }

    /*地形图层选择*/
    private void showDxtLayerSelect(final CheckBox dxtcheckbox,final List<File> list) {
        controlView.getDxtLayerView().setVisibility(View.VISIBLE);
        initDxtCheckBoxData(list);
        final DxtTucengAdapter adapter = new DxtTucengAdapter((BaseActivity) mContext,list,dxtCheckMap,controlView.getDxtTitleLayer());
        ListView listView = (ListView) controlView.getDxtLayerView().findViewById(R.id.img_tcselector);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                File file = list.get(position);
                boolean isChecked = dxtCheckMap.get(file.getName());
                if (isChecked) {
                    dxtCheckMap.put(file.getName(), false);
                    controlView.getMapView().removeLayer(controlView.getDxtTitleLayer().get(file));
                    controlView.getDxtTitleLayer().remove(file);
                } else {
                    dxtCheckMap.put(file.getName(), true);

                    if(!file.exists()){
                        ToastUtil.setToast(mContext, "地形数据文件不存在");
                        return;
                    }
                    //移除GraphicLayer 图层
                    controlView.removeGraphicLayer();
                    //移除矢量图层
                    removeVectorLayer();
                    //添加地形图
                    controlView.addDxtLayer(file);
                    //添加矢量数据图层
                    addVectorLayer();
                    //添加GraphicLayer图层
                    controlView.addGraphicLayer();

                }
                adapter.notifyDataSetChanged();
            }
        });

        ImageView imageView = (ImageView) controlView.getDxtLayerView().findViewById(R.id.btselect_info_close);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                controlView.getDxtLayerView().setVisibility(View.GONE);
                if(controlView.getDxtTitleLayer().size() == 0){
                    dxtcheckbox.setChecked(false);
                }
            }
        });
    }

    /** 图层控制  根据区县 加载otms中资源数据 */
    private void initOtmsData(String qname) {
        final List<File> groups = MyApplication.resourcesManager.getOtmsFolder(controlView.getSysLayerData());
        if(groups == null || groups.size() ==0){
            return;
        }

        final List<Map<String, List<File>>> childs = MyApplication.resourcesManager.getChildeData(mContext,groups);
        if (childs == null || childs.size() == 0)
            return;

        final ExpandableListView tc_exp = (ExpandableListView) childView.findViewById(R.id.tc_expandlistview);
        tc_exp.setGroupIndicator(null);
        int header = tc_exp.getHeaderViewsCount();
        TextView tv_footer = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_expandable_group_top, null);;
        if(header == 0){
            tc_exp.addHeaderView(tv_footer);
        }
        initCheckbox(groups, childs);

        final ExpandableAdapter expandableAdapter = new ExpandableAdapter((BaseActivity)mContext, groups, childs, controlView.getLayerCheckBox());
        tc_exp.setAdapter(expandableAdapter);

        tv_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToastUtil.setToast(mContext,"点击了顶部按钮");
                AddLayerDialog addLayerDialog = new AddLayerDialog(mContext, R.style.Dialog, controlView, new AddLayerDialog.MyListener() {
                    @Override
                    public void addFile(List<File> file, Map<String, List<File>> map) {
                        groups.addAll(file);
                        childs.add(map);
                        expandableAdapter.notifyDataSetChanged();

                    }
                });
                BussUtil.setDialogParams(mContext, addLayerDialog, 0.45, 0.6);

                expandableAdapter.notifyDataSetChanged();
            }
        });

//        tc_exp.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                ScrollViewUtil.setListViewHeightBasedOnChildren(tc_exp,mContext);
//            }
//        });
//        tc_exp.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//            @Override
//            public void onGroupCollapse(int groupPosition) {
//                ScrollViewUtil.setListViewHeightBasedOnChildren(tc_exp,mContext);
//            }
//        });

        //setExpendHeight(expandableAdapter, tc_exp);
        //ScrollViewUtil.setListViewHeightBasedOnChildren(tc_exp,mContext);

        tc_exp.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,int gPosition, int cPosition, long id) {
                CheckBox cBox = (CheckBox) v.findViewById(R.id.cb_child);
                cBox.toggle();// 切换CheckBox状态！！！！！！！！！！

                String parentName = groups.get(gPosition).getName();
                String childName = childs.get(gPosition).get(parentName).get(cPosition).getName().split("\\.")[0];
                String path = childs.get(gPosition).get(parentName).get(cPosition).getPath();

                boolean ischeck = controlView.getLayerCheckBox().get(path);
                changeCBoxStatus(ischeck, path, parentName, childName);

                expandableAdapter.notifyDataSetChanged();// 通知数据发生了变化
                return false;
            }
        });
    }

    private void initImgCheckBoxData(List<File> list) {
        if (imgCheckMap.size() == 0) {
            for (File file : list) {
                imgCheckMap.put(file.getName(), false);
            }
        }
    }

    private void initDxtCheckBoxData(List<File> list) {
        if (dxtCheckMap.size() == 0) {
            for (File file : list) {
                dxtCheckMap.put(file.getName(), false);
            }
        }
    }

    /** 初始化数据选择 */
    public void initCheckbox(List<File> groups,List<Map<String, List<File>>> childs) {

        if (controlView.getLayerCheckBox().size() == 0) {
			/* 第一次初始化数据 */
            for (int i = 0; i < groups.size(); i++) {// 初始时,让所有的子选项均未被选中
                String gname = groups.get(i).getName();
                for (int k = 0; k < childs.size(); k++) {
                    List<File> list = childs.get(k).get(gname);
                    if (list == null)
                        continue;
                    for (File file : list) {
                        String path = file.getPath();
                        controlView.getLayerCheckBox().put(path, false);
                        controlView.getLayerKeyList().add(path);
                    }
                    break;
                }
            }
        } else {
            controlView.getLayerKeyList().clear();
            for (int i = 0; i < groups.size(); i++) {// 初始时,让所有的子选项均未被选中
                String gname = groups.get(i).getName();
                for (int k = 0; k < childs.size(); k++) {
                    List<File> list = childs.get(k).get(gname);
                    if (list == null)
                        continue;
                    for (File file : list) {
                        String path = file.getPath();
                        if(controlView.getLayerCheckBox().get(path) == null){
                            controlView.getLayerCheckBox().put(path, false);
                            controlView.getLayerKeyList().add(path);
                        }else{
                            boolean flag = controlView.getLayerCheckBox().get(path);
                            if (flag) {
                                controlView.getLayerKeyList().add(path);
                            } else {
                                controlView.getLayerKeyList().add(path);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    /**动态设置控件的高度*/
    private void setExpendHeight(ExpandableAdapter adapter,ExpandableListView listview){
        int listViewHeight = 0;
        int adaptCount = adapter.getGroupCount();
        for(int i=0;i<adaptCount;i++){
            View temp = adapter.getGroupView(i, true, null, listview);
            temp.measure(0,0);
            listViewHeight += temp.getMeasuredHeight();
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) listview.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.FILL_PARENT;
        ScreenTool.Screen screen = ScreenTool.getScreenPix(mContext);
        if(listViewHeight > screen.getHeightPixels()/2){
            layoutParams.height = screen.getHeightPixels()/2-60;
        }else{
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
        listview.setLayoutParams(layoutParams);
    }

    /** checkbox状态变化 */
    public void changeCBoxStatus(boolean flag, final String path,final String gpname, final String childName) {
        if(flag){
            controlView.getLayerCheckBox().put(path, false);
            for (int j = 0; j < controlView.getLayerNameList().size();j++) {
                String gname = controlView.getLayerNameList().get(j).getPname();
                String cname = controlView.getLayerNameList().get(j).getCname();
                if(gpname.equals(gname)&&childName.equals(cname)){
                    controlView.getMapView().removeLayer(controlView.getLayerNameList().get(j).getLayer());
                    controlView.getLayerNameList().remove(controlView.getLayerNameList().get(j));
                    j--;
                }
            }

            Iterator<GeodatabaseFeature> it = BaseActivity.selGeoFeaturesList.iterator();
            while (it.hasNext()) {
                MyLayer myLayer = BaseUtil.getIntance(mContext).getFeatureInLayer(it.next(),BaseActivity.layerNameList,BaseActivity.selMap);
                if (myLayer == null) {
                    it.remove();
                }
            }
        }else{
			/*数据加载之前判断数据是否加密*/
            boolean flag2 = SytemUtil.checkGeodatabase(path);
            if (flag2) {
                SytemUtil.decript(path);
            }
            loadGeodatabase(path,flag2,gpname,childName);
        }
    }

    /** 加载geodatabase数据 */
    Geodatabase geodatabase;
    private void loadGeodatabase(String path, boolean flag, String gname,String cname) {
        try {
            geodatabase = new Geodatabase(path);
        } catch (FileNotFoundException | RuntimeException e) {
            e.printStackTrace();
            String error = e.getMessage();
            ToastUtil.setToast(mContext,"数据错误 "+error);
            return;
        }
        if (geodatabase == null){
            ToastUtil.setToast(mContext,"数据错误");
            return;
        }

        boolean ff = false;
        List<GeodatabaseFeatureTable> list = geodatabase.getGeodatabaseTables();
        for (GeodatabaseFeatureTable gdbFeatureTable : list) {
            if (gdbFeatureTable.hasGeometry()) {
                final FeatureLayer layer = new FeatureLayer(gdbFeatureTable);
                Symbol symbol = SytemUtil.getDefaultSymbo((BaseActivity) mContext,layer);

                if(controlView.getBaseTitleLayer() != null && controlView.getBaseTitleLayer().isInitialized()){
                    SpatialReference sp1 = controlView.getBaseTitleLayer().getSpatialReference();
                    SpatialReference sp2 = layer.getDefaultSpatialReference();
                    if(!sp1.equals(sp2)){
                        ToastUtil.setToast(mContext, "加载数据与基础底图投影系不同,无法加载");
                        continue;
                    }
                }
                Renderer renderer = layer.getRenderer();
                Renderer renderer1 = getHisSymbol(layer);
                layer.setRenderer(renderer1);
                ff = true;
                controlView.getMapView().addLayer(layer);

                MyLayer myLayer = new MyLayer();
                myLayer.setPname(gname);
                myLayer.setCname(cname);
                myLayer.setPath(path);
                myLayer.setFlag(flag);
                myLayer.setLname(layer.getName());
                myLayer.setRenderer(renderer);
                myLayer.setLayer(layer);
                myLayer.setSymbol(symbol);
                myLayer.setTable(gdbFeatureTable);
                controlView.getLayerNameList().add(myLayer);
            }
        }

        controlView.removeGraphicLayer();
        controlView.addGraphicLayer();

        controlView.getMapView().invalidate();
        if(ff){
            ProgressDialogUtil.startProgressDialog(mContext);
            controlView.getLayerCheckBox().put(path, true);
        }

        /**检测数据文件夹下是否有config.xml文件如果没有复制进去*/
        String fpath = new File(path).getParent();
        File file = new File(fpath+"/config.xml");
        if(!file.exists()){
            Util.copyFile(mContext, fpath, "config_oo.xml", "config.xml");
        }
    }

    /**获取历史Renderer*/
    public Renderer getHisSymbol(FeatureLayer layer){

        int txt = controlView.getSharedPreferences().getInt(layer.getName()+"tmd", 50);
        //SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(sharedPreferences.getInt("color", Color.GREEN));
        int tcs = controlView.getSharedPreferences().getInt(layer.getName()+"tianchongse", Color.GREEN);
        int tcolor = Util.getColor(tcs, txt);//3158064  959459376
        SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(tcolor);
        Object obj = controlView.getSharedPreferences().getFloat(layer.getName()+"owidth", 4);
        float owidth = 0;
        if(obj != null){
            String str = obj.toString();
            boolean flag = Util.CheckStrIsDouble(str);
            if(flag){
                owidth = Float.parseFloat(str);
            }
        }
        int bjs = controlView.getSharedPreferences().getInt(layer.getName()+"bianjiese", Color.RED);
        simpleFillSymbol.setOutline(new SimpleLineSymbol(bjs, owidth));

        Renderer renderer = null;
        if(layer.getGeometryType().equals(Geometry.Type.POLYGON)){
            boolean flag = false;
            List<com.esri.core.map.Field> fields = layer.getFeatureTable().getFields();
            for (com.esri.core.map.Field ff : fields){
                if(ff.getName().equals("aaa")){
                    flag = true;
                    break;
                }
            }

            if(flag){
                UniqueValueRenderer uniqueValueRenderer = new UniqueValueRenderer();
                uniqueValueRenderer.setField1("aaa");
                UniqueValue uniqueValue = new UniqueValue();
                String[] uniqueAttribute1 = new String[]{"0"};
                uniqueValue.setDescription("0");
                uniqueValue.setValue(uniqueAttribute1);
                uniqueValue.setSymbol(simpleFillSymbol);
                uniqueValueRenderer.addUniqueValue(uniqueValue);

                UniqueValue uniqueValue2 = new UniqueValue();
                String[] uniqueAttribute2 = new String[]{"1"};
                uniqueValue2.setDescription("0");
                uniqueValue2.setValue(uniqueAttribute2);
                SimpleFillSymbol fillSymbol = new SimpleFillSymbol(Util.getColor(Color.rgb(3,104,48), txt));
                fillSymbol.setOutline(new SimpleLineSymbol(bjs, owidth));
                uniqueValue2.setSymbol(fillSymbol);
                uniqueValueRenderer.addUniqueValue(uniqueValue2);

                Symbol symbol = SytemUtil.getDefaultSymbo((BaseActivity) mContext,layer);
                uniqueValueRenderer.setDefaultSymbol(symbol);

                renderer = uniqueValueRenderer;
            }else{
                renderer = new SimpleRenderer(simpleFillSymbol);
            }
        }else if(layer.getGeometryType().equals(Geometry.Type.POLYLINE)){
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(tcolor, (int) owidth, com.esri.core.symbol.SimpleLineSymbol.STYLE.SOLID);
            lineSymbol.setWidth(owidth);
            lineSymbol.setColor(tcolor);
            renderer = new SimpleRenderer(lineSymbol);
        }else{
            SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(tcolor, (int) owidth, SimpleMarkerSymbol.STYLE.CIRCLE);
            markerSymbol.setOutline(new SimpleLineSymbol(bjs, owidth));
            markerSymbol.setColor(bjs);
            renderer = new SimpleRenderer(markerSymbol);
        }

        return renderer;
    }

    /** 缩放至基础图层所在位置 */
    public void zoomImageLayer() {
        if(controlView.getImgTitleLayer().size() == 0){
            ToastUtil.setToast(mContext, "地形图图未加载,请在图层控制中加载数据");
            return;
        }
        if(controlView.getImgTitleLayer().size() == 1){
            for(File file : controlView.getImgTitleLayer().keySet()){
                controlView.getMapView().setExtent(controlView.getImgTitleLayer().get(file).getFullExtent());
            }
            controlView.getMapView().invalidate();
            return;
        }

        Geometry[] geometries = new Geometry[controlView.getImgTitleLayer().size()];
        for(int i=0;i<controlView.getImgTitleLayer().size();i++){
            geometries[i] = controlView.getImgTitleLayer().get(i).getFullExtent();
        }
        Geometry env = GeometryEngine.union(geometries, SpatialUtil.defalutSpatialReference());
        controlView.getMapView().setExtent(env);
        controlView.getMapView().invalidate();
    }

}
