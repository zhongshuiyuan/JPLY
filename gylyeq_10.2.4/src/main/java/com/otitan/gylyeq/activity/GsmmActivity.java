package com.otitan.gylyeq.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.otitan.gylyeq.BaseActivity;
import com.otitan.gylyeq.R;
import com.otitan.gylyeq.util.BussUtil;
/**
 * Created by li on 2016/5/26.
 * 古树名木页面
 */
public class GsmmActivity extends BaseActivity {

	View parentView;
	GsmmActivity activity;
	Context mContext;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		parentView = getLayoutInflater().inflate(R.layout.activity_gsmm, null);
		super.onCreate(savedInstanceState);
		setContentView(parentView);
		
		mContext = GsmmActivity.this;
		//ImageView topview = (ImageView) parentView.findViewById(R.id.topview);
		//topview.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.share_top_gsmm));

		TextView sysname = parentView.findViewById(R.id.system_name);
		if(sysname != null){
			sysname.setText(mContext.getResources().getString(R.string.gsmm_name));
		}

		activitytype = getIntent().getStringExtra("name");
		proData = BussUtil.getConfigXml(mContext,activitytype);
	}

	@Override
	public View getParentView() {
		return parentView;
	}
	
}
