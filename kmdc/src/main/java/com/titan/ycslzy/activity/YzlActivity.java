package com.titan.ycslzy.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.titan.ycslzy.BaseActivity;
import com.titan.ycslzy.R;
import com.titan.ycslzy.util.BussUtil;

/**
 * Created by li on 2016/5/26.
 * 营造林页面
 */
public class YzlActivity extends BaseActivity {
	
	private View parentView;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		parentView = getLayoutInflater().inflate(R.layout.activity_yzl, null);
		super.onCreate(savedInstanceState);
		setContentView(parentView);

		Context mContext = YzlActivity.this;
		//ImageView topview = (ImageView) parentView.findViewById(R.id.topview);
		//topview.setBackground(mContext.getResources().getDrawable(R.drawable.share_top_yzl));
		
		activitytype = getIntent().getStringExtra("name");
		proData = BussUtil.getConfigXml(mContext,"yzl");
		
	}

	@Override
	public View getParentView() {
		return parentView;
	}

}
