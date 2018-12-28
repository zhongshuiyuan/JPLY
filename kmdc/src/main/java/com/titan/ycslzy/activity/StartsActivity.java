package com.titan.ycslzy.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.titan.ycslzy.BaseActivity;
import com.titan.ycslzy.R;
import com.titan.ycslzy.util.BussUtil;

/**
 * 首页
 */
public class StartsActivity extends BaseActivity {

    private View parentView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        parentView = getLayoutInflater().inflate(R.layout.activity_starts, null);
        super.onCreate(savedInstanceState);
        setContentView(parentView);

        Context mContext = StartsActivity.this;
        TextView topview = (TextView) parentView.findViewById(R.id.topview);
        //topview.setBackground(mContext.getResources().getDrawable(R.drawable.share_top_yzl));

        activitytype = getIntent().getStringExtra("name");
        proData = BussUtil.getConfigXml(mContext,"yzl");
    }

    @Override
    public View getParentView() {
        return parentView;
    }
}
