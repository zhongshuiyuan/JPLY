package com.titan.ycslzy.presenter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geometry.Envelope;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.tasks.query.QueryParameters;
import com.titan.ycslzy.R;
import com.titan.ycslzy.adapter.LayerLableAdapter;
import com.titan.ycslzy.entity.MyLayer;
import com.titan.ycslzy.mview.ILayerView;
import com.titan.ycslzy.util.BaseUtil;

import java.util.HashMap;
import java.util.List;


/**
 * Created by li on 2017/6/5.
 * 图层标注presenter
 */

public class LayerLablePresenter {

    private Context mContext;
    private ILayerView iLayerView;
    public MyLayer myLayer;
    private long[] arrays;
    private HashMap<Field,Boolean> checkboxs = new HashMap<>();

    public LayerLablePresenter(Context context, ILayerView layerView){
        this.mContext = context;
        this.iLayerView = layerView;
    }

    /**显示选择图层的对应字段*/
    public void showLayerAials(final View lableView, MyLayer myLayer){
        this.myLayer = myLayer;
        ImageView imageView = (ImageView) lableView.findViewById(R.id.attr_field_exit);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lableView.setVisibility(View.GONE);
            }
        });
        ListView listView =(ListView) lableView.findViewById(R.id.field_list);
        List<Field> fields = myLayer.getLayer().getFeatureTable().getFields();
        for(Field f:fields){
            checkboxs.put(f,false);
        }
        LayerLableAdapter lableAdapter = new LayerLableAdapter(mContext,fields,this,checkboxs);
        listView.setAdapter(lableAdapter);
        BaseUtil.setHeight(lableAdapter,listView);

    }

    /**查询当前区域内的对应图层的小班数据*/
    public void queryFeatures(final MyLayer myLayer){
        QueryParameters q = new QueryParameters();
        q.setWhere("1=1");
        q.setInSpatialReference(iLayerView.getSpatialReference());
        q.setReturnGeometry(true);
        q.setGeometry(iLayerView.getCurrentEnvelope());
        myLayer.getLayer().setSelectionColor(0);
        myLayer.getLayer().selectFeatures(q, FeatureLayer.SelectionMode.NEW, new CallbackListener<FeatureResult>() {
            @Override
            public void onCallback(FeatureResult featureResult) {
                if (featureResult.featureCount() > 0){
                    arrays = myLayer.getLayer().getSelectionIDs();
                }
            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }

    /**添加lable*/
    public void addLableToLayer(boolean isChecked, List<Field> fields, int position){
        if(isChecked){
            iLayerView.getGraphicLayer().removeAll();
            return;
        }
        if(arrays == null){
            return;
        }
        iLayerView.getGraphicLayer().removeAll();
        for(long id : arrays){
            GeodatabaseFeature feature = (GeodatabaseFeature) myLayer.getLayer().getFeature(id);
            Object obj = feature.getAttributeValue(fields.get(position));
            if(obj != null){
                TextSymbol textSymbol = new TextSymbol(20,obj.toString(), Color.BLUE);
                Envelope env = new Envelope();
                feature.getGeometry().queryEnvelope(env);
                Graphic graphic = new Graphic(env.getCenter(),textSymbol);
                iLayerView.getGraphicLayer().addGraphic(graphic);
            }
        }
    }
}
