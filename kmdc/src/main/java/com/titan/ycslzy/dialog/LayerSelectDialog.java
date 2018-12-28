package com.titan.ycslzy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.titan.ycslzy.R;
import com.titan.ycslzy.adapter.LayerAdapter;
import com.titan.ycslzy.entity.ActionMode;
import com.titan.ycslzy.entity.MyLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2017/6/2.
 * 图层选择dialog
 */

public class LayerSelectDialog extends Dialog implements ListView.OnItemClickListener{

    private Context mContext;
    private List<MyLayer> layerList = new ArrayList<>();
    private SetOnItemClickListener setOnItemClickListener;
    private ActionMode actionMode;

    public LayerSelectDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public LayerSelectDialog(@NonNull Context context, @StyleRes int themeResId,List<MyLayer> list,ActionMode mode) {
        super(context, themeResId);
        this.mContext = context;
        this.layerList = list;
        this.actionMode = mode;
    }

    protected LayerSelectDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_featureselect);
        setCanceledOnTouchOutside(true);

        ListView listview = (ListView) findViewById(R.id.listview_layers);
        LayerAdapter adapter = new LayerAdapter(layerList, mContext);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }

    public void setLayerOnItemClickListener(SetOnItemClickListener layerOnItemClickListener){
        if(layerOnItemClickListener != null){
            this.setOnItemClickListener = layerOnItemClickListener;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setOnItemClickListener.setLayerOnItemClickListener(parent, view, position, id,actionMode,layerList,this);
    }

    public interface SetOnItemClickListener{
       void setLayerOnItemClickListener(AdapterView<?> parent, View view, int position, long id, ActionMode mode, List<MyLayer> layerList, LayerSelectDialog layerSelectDialog);
    }
}
