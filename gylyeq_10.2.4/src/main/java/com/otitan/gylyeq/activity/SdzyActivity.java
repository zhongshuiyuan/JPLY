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
 * 湿地资源页面
 */
public class SdzyActivity extends BaseActivity {

	View parentview;
	Context mContext;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		parentview = getLayoutInflater().inflate(R.layout.activity_sdzy, null);
		super.onCreate(savedInstanceState);
		setContentView(parentview);
		mContext = SdzyActivity.this;
		/*变更系统背景*/
		//ImageView topview = (ImageView) parentview.findViewById(R.id.topview);
		//topview.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.share_top_sdzy));

		TextView sysname = parentview.findViewById(R.id.system_name);
		if(sysname != null){
			sysname.setText(mContext.getResources().getString(R.string.sdzy_name));
		}

		activitytype = getIntent().getStringExtra("name");
		/*获取配置的数据*/
		proData = BussUtil.getConfigXml(mContext,activitytype);
		
	}

	@Override
	public View getParentView() {
		return parentview;
	}

}
