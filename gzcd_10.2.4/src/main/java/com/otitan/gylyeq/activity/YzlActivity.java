package com.otitan.gylyeq.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.otitan.gylyeq.BaseActivity;
import com.otitan.gylyeq.R;
import com.otitan.gylyeq.util.BussUtil;
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
		ImageView topview = (ImageView) parentView.findViewById(R.id.topview);
		//topview.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.share_top_yzl));
		topview.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.share_top_cd));
		activitytype = getIntent().getStringExtra("name");
		activitytype = "yzl";
		proData = BussUtil.getConfigXml(mContext,activitytype);
		
	}

	@Override
	public View getParentView() {
		return parentView;
	}

}
