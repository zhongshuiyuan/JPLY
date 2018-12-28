package com.otitan.gylyeq.edite.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.FeatureLayer.SelectionMode;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Geometry.Type;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.FeatureTemplate;
import com.esri.core.map.FeatureType;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;
import com.esri.core.table.TableException;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;
import com.otitan.gylyeq.BaseActivity;
import com.otitan.gylyeq.MyApplication;
import com.otitan.gylyeq.R;
import com.otitan.gylyeq.adapter.EdFeatureResultAdapter;
import com.otitan.gylyeq.adapter.LayerAdapter;
import com.otitan.gylyeq.entity.MyFeture;
import com.otitan.gylyeq.entity.MyLayer;
import com.otitan.gylyeq.listviewinedittxt.Line;
import com.otitan.gylyeq.listviewinedittxt.LineAdapter;
import com.otitan.gylyeq.model.FormatModel;
import com.otitan.gylyeq.model.imodel.IFormatModel;
import com.otitan.gylyeq.util.BussUtil;
import com.otitan.gylyeq.util.CursorUtil;
import com.otitan.gylyeq.util.SpatialUtil;
import com.otitan.gylyeq.util.ToastUtil;
import com.otitan.gylyeq.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 小班数据编辑
 */
public class XbEditActivity extends BaseEditActivity {

	private ArrayList<Line> mLines;
	private LineAdapter mAdapter;
	private Context mContext;
	/** 工程所在地址 */
	private TextView btnreturn,photograph,seepicture,txtviewxbh,tvyddcb,xbarea,yddlb;
	private String yddLayername = "";

	private MyLayer ydLayer;
	private long yddId = 0;
	private FeatureTemplate layerTemplate;
	private Map<String, Object> layerFeatureAts = new HashMap<>();

	public long numSize = 0;
	public List<GeodatabaseFeature> ydlist = new ArrayList<>();
	public Map<GeodatabaseFeature, MyLayer> ydMap = new HashMap<>();
	protected static final int TAKE_PICTURE_1 = 0x0000011;

	private IFormatModel formatModel = new FormatModel();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		childView = getLayoutInflater().inflate(R.layout.fragment_notepad,null);
		super.onCreate(savedInstanceState);
		setContentView(childView);

		mContext = XbEditActivity.this;

		picPath = getImagePath(path);

		listView = (ListView) childView.findViewById(R.id.listView_xbedit);
		if (savedInstanceState == null) {
			mLines = createLines();
		} else {
			mLines = savedInstanceState.getParcelableArrayList(EXTRA_LINES);
		}
		mAdapter = new LineAdapter(XbEditActivity.this,mLines,myFeture);
		listView.setAdapter(mAdapter);

		btnreturn = (TextView) findViewById(R.id.btnreturn);
		btnreturn.setOnClickListener(new MyListener());
		photograph = (TextView) findViewById(R.id.photograph);
		photograph.setOnClickListener(new MyListener());
		seepicture = (TextView) findViewById(R.id.ld_see_pic);
		seepicture.setOnClickListener(new MyListener());

		xbarea = (TextView) findViewById(R.id.tv_xbarea);
		double area = selGeoFeature.getGeometry().calculateArea2D();
		String txt = df.format(Math.abs(area))+"/"+df.format(Math.abs(area*0.0015))+ "/"+df.format(Math.abs(area*0.0001))+"  平方米/亩/公顷";
		xbarea.setText(txt);
		//txtviewxbh = (TextView) findViewById(R.id.tv_xbh);

		//getXbhData(pname);

		cpoyZhaop();
		//new MyAsyncTask().execute("getImagePath");

		if(currentxbh.equals("")){
			ToastUtil.setToast(mContext, "小班唯一号为空,请输入小班唯一号");
		}

		if(pname.contains("营造林")){

		}

		tvyddcb = (TextView) findViewById(R.id.tvyddcb);
		//tvyddcb.setVisibility(View.VISIBLE);
		tvyddcb.setOnClickListener(new MyListener());

		yddlb = (TextView) findViewById(R.id.yangdidian);
		yddlb.setOnClickListener(new MyListener());

		//getMustField();
	}


	@Override
	public View getParentView() {
		return childView;
	}

	class MyListener implements View.OnClickListener{

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.btnreturn:
					finishThis();
					break;
				case R.id.ld_see_pic:
					/*图片浏览*/
					lookpictures(XbEditActivity.this);
					break;
				case R.id.tvyddcb:
					/* 添加样地点 */
					getYdLayer();
					showYddLayer();
					break;
				case R.id.yangdidian:
					//样地列表
					getYdLayer();
					getGeometryInfo(selGeoFeature.getGeometry(),ydLayer,true);
					break;
				case R.id.photograph:
					takephoto();
					break;
				default:
					break;
			}
		}
	}

	public void takephoto(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		@SuppressWarnings("static-access")
		String time = (String) new DateFormat().format("yyyyMMddHHmmss",Calendar.getInstance(Locale.CHINA));

		String picname = "";
		if(currentxbh != null && !currentxbh.equals("")){
			List<File> lst = MyApplication.resourcesManager.getImages(picPath);
			picname = currentxbh+"_"+(lst.size()+1)+".jpg";
		}else{
			picname = time+".jpg";
		}

		File file = new File(picPath +"/"+picname);
		file = picIsExst(file);
		mCurrentPhotoPath = file.getPath();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			try {
				//改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
				Uri photoURI = FileProvider.getUriForFile(mContext, this.getApplicationContext().getPackageName() + ".provider", file);
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
				if (photoURI != null) {
					intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
					intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		}

		startActivityForResult(intent, TAKE_PICTURE_1);
	}
	/** 样地调查表数据填写*/
	public void yddcb(final String ydlayerName){
		Builder builder = new Builder(mContext);
		builder.setMessage("使用当前点!");
		builder.setTitle("信息提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Point point = BaseActivity.currentPoint;
				addYddFeatureOnLayer(dialog,point,yddLayername);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				XbEditActivity.this.finish();
			}
		});
		builder.create().show();

	}
	/**展示样地点图层*/
	public void showYddLayer(){
		List<MyLayer> list = new ArrayList<MyLayer>();
		for(MyLayer myLayer : BaseActivity.layerNameList){
			Type type = myLayer.getTable().getGeometryType();
			String name = myLayer.getCname();
			if(type.equals(Geometry.Type.POINT) && cname.equals(name)){
				list.add(myLayer);
			}
		}
		int size = list.size();
		if (size == 1) {
			if(BaseActivity.currentPoint == null || !BaseActivity.currentPoint.isValid()){
				ToastUtil.setToast(mContext, "未获取到当前位置坐标");
				return;
			}
			MyLayer layer = list.get(0);
			getGeometryInfo(selGeoFeature.getGeometry(),layer,false);
			yddLayername = layer.getTable().getTableName();
			boolean flag = GeometryEngine.intersects(selGeoFeature.getGeometry(), BaseActivity.currentPoint, BaseActivity.mapView.getSpatialReference());
			if(flag){
				yddcb(yddLayername);
			}else{
				ToastUtil.setToast(mContext, "当前位置不在所选小班范围内");
			}
		}else{
			if (size > 1) {
				showFeatureLayer(list);
			}
		}
	}

	/** 编辑图层选择窗口， 选择要编辑的图层 */
	public void showFeatureLayer(final List<MyLayer> list) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_featureselect);
		dialog.setCanceledOnTouchOutside(true);

		ListView listview = (ListView) dialog.findViewById(R.id.listview_layers);
		LayerAdapter adapter = new LayerAdapter(list, mContext);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int position, long arg3) {
				if(BaseActivity.currentPoint == null || !BaseActivity.currentPoint.isValid()){
					ToastUtil.setToast(mContext, "未获取到当前位置坐标");
					return;
				}

				MyLayer layer = list.get(position);
				getGeometryInfo(selGeoFeature.getGeometry(),layer,false);
				yddLayername = layer.getTable().getTableName();
				dialog.dismiss();
				boolean flag = GeometryEngine.intersects(selGeoFeature.getGeometry(), BaseActivity.currentPoint, BaseActivity.mapView.getSpatialReference());
				if(flag){
					yddcb(yddLayername);
				}else{
					ToastUtil.setToast(mContext, "当前位置不在所选小班范围内");
				}
			}
		});

		BussUtil.setDialogParams(mContext, dialog, 0.5, 0.5);
	}

	/**更新照片编号*/
	public void updateZp(String pctext,Line line,String bfText){
		this.pcLine = line;
		updateJjzp(pctext,line,bfText);
	}

	public void updateJjzp(String pctext,Line line,String bfText){
		GeodatabaseFeature feature = (GeodatabaseFeature) featureLayer.getFeature(selGeoFeature.getId());
		Map<String, Object> attribute = feature.getAttributes();
		boolean flag = true;
		if(pctext == null || (bfText != null && pctext.equals(bfText))){
			return;
		}

		int length = pctext.length();
		int size = 0;
		for(Field ff : fieldList){
			if(ff.getName().equals(line.getKey())){
				size = ff.getLength();
				break;
			}
		}

		if(length > size){
			ToastUtil.setToast(mContext, "数据长度超过数据库规定长度");
			return;
		}

		attribute.put(line.getKey(), pctext);
		String error = "数据错误";
		//Graphic updateGraphic = new Graphic(feature.getGeometry(),feature.getSymbol(),attribute);
		GeodatabaseFeatureTable featureTable = (GeodatabaseFeatureTable)featureLayer.getFeatureTable();
		try
		{
			long id = feature.getId();
			GeodatabaseFeature geodatabaseFeature = new GeodatabaseFeature(attribute, featureTable);
			featureTable.updateFeature(id, geodatabaseFeature);
		} catch (TableException e)
		{
			flag = false;
			e.printStackTrace();
			error = e.getMessage();
		}
		if(flag){
			line.setText(pctext);
			mAdapter.notifyDataSetChanged();
			ToastUtil.setToast(mContext, "照片编号更新成功");
		}else{
			ToastUtil.setToast(mContext, "照片编号更新失败 "+error);
		}
	}


	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {

			dealPhotoFile(mCurrentPhotoPath);

			updateZPBH();
		}else if(requestCode == TAKE_PICTURE_1 && resultCode == Activity.RESULT_OK){
			dealPhotoFile(mCurrentPhotoPath);
		}
	}

	/** 选择小班查询方法 */
	public void getGeometryInfo(Geometry geometry, final MyLayer layer, final boolean isshow) {
		ydlist.clear();
		ydMap.clear();
		numSize = 0;
		QueryParameters queryParams = new QueryParameters();
		queryParams.setOutFields(new String[] { "*" });
		queryParams.setSpatialRelationship(SpatialRelationship.INTERSECTS);
		queryParams.setGeometry(geometry);
		queryParams.setReturnGeometry(true);
		queryParams.setOutSpatialReference(SpatialUtil.defalutSpatialReference());
		layer.getLayer().selectFeatures(queryParams, SelectionMode.NEW,
				new CallbackListener<FeatureResult>() {

					@Override
					public void onError(Throwable arg0) {
						ToastUtil.setToast(mContext, "查询出错");
					}

					@Override
					public void onCallback(FeatureResult result) {
						if(result.featureCount() > 0){
							numSize = result.featureCount();
							Iterator<Object> iterator = result.iterator();
							while (iterator.hasNext()) {
								GeodatabaseFeature feature = (GeodatabaseFeature) iterator.next();
								ydlist.add(feature);
								ydMap.put(feature, layer);
							}
							if(isshow){
								new Handler(Looper.getMainLooper()).post(new Runnable() {
									@Override
									public void run() {
										// 此处执行UI操作
										getYangdi();
									}
								});
							}
						}else{
							ToastUtil.setToast(mContext, "当前小班没有样地点");
						}
					}
				});
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		finishThis();
	}

	/**更新照片编号*/
	public void updateEdittxt(){
		runOnUiThread(new Runnable() {
			public void run() {
				String bftxt = "";
				if(pcLine != null){
					bftxt = pcLine.getText();
				}

				if(bftxt == null || bftxt.equals("")){
					File file = new File(mCurrentPhotoPath);
					if(file.exists()){
						zpeditText.setText(file.getName());
					}
				}else{
					File file = new File(mCurrentPhotoPath);
					if(file.exists()){
						zpeditText.setText(bftxt+","+ file.getName());
					}
				}
				CursorUtil.setEditTextLocation(zpeditText);
			}
		});
	}

	/** 异步类 */
	class MyAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(final String... params) {
			if (params[0].equals("getImagePath")) {
				cpoyZhaop();
			}
			return null;
		}
	}
	/**检查照片编号是否与唯一编号一致*/
	public void cpoyZhaop(){
		Map<String, Object> attrs = myFeture.getFeature().getAttributes();
		String Ywybh = "";
		for(String str : attrs.keySet()){
			Object obj = attrs.get(str);
			if(obj != null && obj.toString().trim().contains(".jpg")){
				String[] strs = obj.toString().trim().split("_");
				if(strs.length > 1){
					Ywybh = strs[0];
					break;
				}
			}
		}

		if(!Ywybh.equals("") && !Ywybh.equals(currentxbh)){
			File file = new File(picPath);
			File file2 = new File(file.getParent()+"/"+Ywybh);
			if(file2.exists()){
				File[] files = file2.listFiles();
				for(File ff : files){
					File file3 = new File(picPath, ff.getName());
					copyFile(ff, file3);
				}
			}
		}
	}

	/**复制文件到平板中*/
	private void copyFile(File sourceFile, File targetFile){
		try {
			InputStream db = new FileInputStream(sourceFile);
			FileOutputStream fos = new FileOutputStream(targetFile);
			byte[] buffer = new byte[8129];
			int count = 0;

			while ((count = db.read(buffer)) >= 0) {
				fos.write(buffer, 0, count);
			}

			fos.close();
			db.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	protected void onRestart() {
		super.onRestart();
		mAdapter.getGeometryInfo(myFeture.getFeature().getGeometry());
	}

	/**获取样地数据*/
	public void getYangdi(){
		if(numSize > 1){
			showYangdiData();
		}else if(numSize == 1){
			if(parentStr.equals("PointEdit")){
				this.finish();
				return;
			}
			MyLayer layer = ydMap.get(ydlist.get(0));
			GeodatabaseFeature feature = (GeodatabaseFeature) layer.getLayer().getFeature(ydlist.get(0).getId());
			toYangdiInfo(feature,layer);
		}else{
			ToastUtil.setToast(mContext, "不存在样地");
		}
	}

	/**当样地个数大于1个时显示样地列表*/
	public void showYangdiData(){
		Dialog dialog = new Dialog(mContext,R.style.Dialog);
		dialog.setContentView(R.layout.dialog_yd_list);
		ListView yd_lstview = (ListView) dialog.findViewById(R.id.yd_data_listview);

		EdFeatureResultAdapter adapter = new EdFeatureResultAdapter(mContext, ydlist, "样地点列表");
		yd_lstview.setAdapter(adapter);

		yd_lstview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adv, View v, int position,
									long id) {
				MyLayer layer = ydMap.get(ydlist.get(position));
				GeodatabaseFeature feature = (GeodatabaseFeature) layer.getLayer().getFeature(ydlist.get(position).getId());
				if(parentId == feature.getId()){
					finish();
					return;
				}
				toYangdiInfo(feature,ydMap.get(feature));
			}
		});

		BussUtil.setDialogParams(mContext, dialog, 0.5, 0.5);
	}

	/**跳转到样地属性页*/
	public void toYangdiInfo(GeodatabaseFeature feature,MyLayer layer){
		Intent intent = new Intent(mContext, PointEditActivity.class);
		MyFeture feture = new MyFeture(pname, path, cname, feature,layer);
		Bundle bundle = new Bundle();
		bundle.putSerializable("myfeture", feture);
		bundle.putSerializable("parent", "XbEdit");
		bundle.putSerializable("pWybh", currentxbh);
		bundle.putSerializable("id", fid+"");
		intent.putExtras(bundle);
		startActivity(intent);
	}
	/**获取样地所在的图层及样地数据*/
	public void getYdLayer(){
		for(MyLayer myLayer : BaseActivity.layerNameList){
			Type type = myLayer.getTable().getGeometryType();
			String name = myLayer.getCname();
			if(type.equals(Geometry.Type.POINT) && cname.equals(name)){
				ydLayer = myLayer;
				getEditSymbo(ydLayer.getLayer());
			}
		}
	}

	/**
	 * 添加feature在图层上
	 * geom 绘制的图形
	 * selFeatureAts  图层属性
	 */
	public void addYddFeatureOnLayer(DialogInterface dialog,Geometry geom,String ydlayerNmae) {
		try {
			GeodatabaseFeatureTable table = ydLayer.getTable();
			GeodatabaseFeature g = table.createFeatureWithTemplate(layerTemplate, geom);
			Symbol symbol = ydLayer.getRenderer().getSymbol(g);
			// symbol为null也可以 why？
			Map<String, Object> editAttributes = setYddAttribute(g.getAttributes());
			//TODO
			@SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String str = format.format(new Date());
			editAttributes.put("WYBH", str);
			//editAttributes.put("aaa",0);
			//editAttributes = setYddAttribute(editAttributes);
			Graphic addedGraphic = new Graphic(geom, symbol, editAttributes);
			yddId = table.addFeature(addedGraphic);

			Feature feature = table.getFeature(yddId);

			Geometry geometry = feature.getGeometry();
			if (geometry.isEmpty() || !geometry.isValid()) {
				table.deleteFeature(yddId);
			}else{
				//updateFeature(feature.getAttributes());
				Intent intent = new Intent(mContext, YzlYddActivity.class);
				myFeture.setFeature((GeodatabaseFeature)featureLayer.getFeature(selGeoFeature.getId()));
				intent.putExtra("xbh", currentxbh);//小班唯一编号
				intent.putExtra("myfeture", myFeture);
				intent.putExtra("yddname", ydlayerNmae);
				intent.putExtra("numSize", numSize);
				intent.putExtra("id", selGeoFeature.getId() + "");
				intent.putExtra("ydid",yddId+"");
				startActivity(intent);
				dialog.dismiss();
			}
		} catch (TableException e) {
			e.printStackTrace();
			ToastUtil.setToast(mContext,"添加样地点异常");
		}
	}


	/** 获取FeatureLayer图层样式 */
	public void getEditSymbo(FeatureLayer flayer) {
		String typeIdField = ((GeodatabaseFeatureTable) flayer.getFeatureTable()).getTypeIdField();
		if (typeIdField.equals("")) {
			List<FeatureTemplate> featureTemp = ((GeodatabaseFeatureTable) flayer.getFeatureTable()).getFeatureTemplates();

			for (FeatureTemplate featureTemplate : featureTemp) {
				GeodatabaseFeature g;
				try {
					g = ((GeodatabaseFeatureTable) flayer.getFeatureTable()).createFeatureWithTemplate(featureTemplate, null);
					layerTemplate = featureTemplate;
					layerFeatureAts.putAll(g.getAttributes());
				} catch (TableException e) {
					e.printStackTrace();
				}
			}
		} else {
			List<FeatureType> featureTypes = ((GeodatabaseFeatureTable) flayer.getFeatureTable()).getFeatureTypes();
			for (FeatureType featureType : featureTypes) {
				FeatureTemplate[] templates = featureType.getTemplates();
				for (FeatureTemplate featureTemplate : templates) {
					GeodatabaseFeature g;
					try {
						g = ((GeodatabaseFeatureTable) flayer.getFeatureTable()).createFeatureWithTemplate(featureTemplate, null);
						layerTemplate = featureTemplate;
						layerFeatureAts.putAll(g.getAttributes());
					} catch (TableException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private Map<String, Object> setYddAttribute(Map<String, Object> layerFeatureAts){
		int count1 = selGeoFeature.getAttributes().keySet().size();
		List<Field> ydd_fields = ydLayer.getLayer().getFeatureTable().getFields();
		int count2 = ydd_fields.size();

		List<Field> lst1 = selGeoFeature.getTable().getFields();
		for(int k=0;k<count2;k++){
			Field field2 = ydd_fields.get(k);
			String alias2 = field2.getAlias();
			String key2 = field2.getName();
			int lenght = field2.getLength();

			for(int i=0;i<count1;i++){
				Field field1 = lst1.get(i);
				String alias1 = field1.getAlias();
				String key1 = field1.getName();
				if(key2.equals("OBJECTID")){
					break;
				}
				if(key1.equals("OBJECTID")){
					continue;
				}
//				if(alias1.contains("设计造林密度") || key2.contains("SJZLMD")){
//					Object obj1 = selGeoFeature.getAttributeValue(key1);
//					if(obj1 != null){
//						boolean flag = Util.CheckStrIsDouble(obj1.toString());
//						if(flag){
//							//sjmdSize = Double.parseDouble(obj1.toString());
//						}
//					}
//				}

				if(alias1.contains(alias2) || alias2.contains(alias1) || key2.equals(key1)){
					if(field1.getFieldType() == field2.getFieldType()){
						Object obj = selGeoFeature.getAttributes().get(key1);
						if(obj != null){
							if(lenght > obj.toString().length()){
								layerFeatureAts.put(key2,obj);
							}
						}

					}
				}
			}

			Object obj = null;
			if(alias2.contains("横坐标") || alias2.contains("经度")){
				obj = formatModel.decimalFormat(BaseActivity.currentPoint.getX());
			}else if(alias2.contains("纵坐标") || alias2.contains("纬度")){
				obj = formatModel.decimalFormat(BaseActivity.currentPoint.getY());
			}else if(alias2.contains("标准地号") || key2.equals("bzdh")){//标准地号
				String bzdh = currentxbh+"_"+(numSize+1);
				if(bzdh.length() > lenght){
					obj = (numSize+1);
				}else{
					obj = bzdh;
				}
			}else if(alias2.contains("样地类型") || key2.equals("YDLX")){
				obj = "2";
			}

			if(obj != null){
				if(lenght>=obj.toString().length()){
					layerFeatureAts.put(key2, obj);
				}
			}
		}

//		List<Field> fields = ydLayer.getLayer().getFeatureTable().getFields();
//		for(Field field : fields){
//			String key = field.getName();
//			if(key.equals("OBJECTID")){
//				continue;
//			}
//			Object value = layerFeatureAts.get(key);
//			if(value == null){
//				if(field.getFieldType() == Field.esriFieldTypeDouble){
//					layerFeatureAts.put(key,0);
//				}if(field.getFieldType() == Field.esriFieldTypeDate){
//					layerFeatureAts.put(key,new Date());
//				}else{
//					layerFeatureAts.put(key,"");
//				}
//
//			}
//		}

		return layerFeatureAts;
	}

	private void updateFeature(Map<String,Object> attribute){
		GeodatabaseFeatureTable table = ydLayer.getTable();
		// symbol为null也可以 why？
		Map<String, Object> editAttributes = setYddAttribute(attribute);

		try {
			GeodatabaseFeature feature = new GeodatabaseFeature(editAttributes,table);
			table.updateFeature(yddId,feature);
		} catch (TableException e) {
			e.printStackTrace();
		}
	}



}
