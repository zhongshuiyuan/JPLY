package com.otitan.gylyeq.edite.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Geometry.Type;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CodedValueDomain;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureTemplate;
import com.esri.core.map.FeatureType;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.symbol.Symbol;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.otitan.gylyeq.BaseActivity;
import com.otitan.gylyeq.R;
import com.otitan.gylyeq.entity.ActionMode;
import com.otitan.gylyeq.entity.MyFeture;
import com.otitan.gylyeq.entity.MyLayer;
import com.otitan.gylyeq.entity.Row;
import com.otitan.gylyeq.listviewinedittxt.Line;
import com.otitan.gylyeq.listviewinedittxt.YddcLineAdapter;
import com.otitan.gylyeq.util.SytemUtil;
import com.otitan.gylyeq.util.ToastUtil;
import com.otitan.gylyeq.util.Util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小班图片
 */
public class YzlYddActivity extends BaseEditActivity {

	View parentView;
	/** FeatureLayer图层 */
	private FeatureTemplate layerTemplate;
	private List<Field> ydd_fields;
	private Symbol layerSymbol;
	private Renderer layerRenderer;
	private Map<String, Object> layerFeatureAts = new HashMap<>();

	private Context mContext;
	/**与样地点关联的小班唯一号*/
	public String xbwybh;
	public String ydcurrentxbh;
	private long ydid;

	/*样地*/
	public long numSize = 0;
	private long feutureID;
	private YddcLineAdapter mAdapter;
	private ArrayList<Line> mLines;
	private GeodatabaseFeature ydgeoFeature;
	/** 选择图斑的属性信息 */
	//public static Map<String, Object> geoFeatureAts = null;
	//private List<Field> fieldList = null;

	private TextView yddcxbh,mmdc;
	/**返回、图片浏览、拍照*/
	private TextView back,piclook;

	MyFeture xbmyFeture = null;
	private String yddLayername = "";
	DecimalFormat format = new DecimalFormat("0.000000");
	/**小班面*/
	private TextView yddc_xbm;
	/**小班设计密度*/
	public double sjmdSize = 0;
	private MyLayer ydMyLayer = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		parentView = getLayoutInflater().inflate(R.layout.activity_yzlyddc, null);
		super.onCreate(savedInstanceState);
		setContentView(parentView);
		mContext = YzlYddActivity.this;

		xbmyFeture = (MyFeture) getIntent().getSerializableExtra("myfeture");
		yddLayername = getIntent().getStringExtra("yddname");
		xbwybh = getIntent().getStringExtra("xbh");
		String yddid = getIntent().getStringExtra("ydid");
		ydid = Long.parseLong(yddid);
		numSize = getIntent().getLongExtra("numSize", 0);

		double lon = BaseActivity.currentPoint.getX();
		double lat = BaseActivity.currentPoint.getY();

		getLayer();

		ydgeoFeature = (GeodatabaseFeature) ydMyLayer.getLayer().getFeature(ydid);

		if(ydgeoFeature == null){
			com.titan.baselibrary.util.ToastUtil.setToast(mContext,"数据库没有唯一编号");
			this.finish();
			return;
		}
		Object obj = ydgeoFeature.getAttributes().get("WYBH");
		if(obj != null){
			ydcurrentxbh = obj.toString();
		}

		picPath = getImagePath(path);
		listView = (ListView) findViewById(R.id.listView_xbedit);
		if (savedInstanceState == null) {
			mLines = createLines();
		} else {
			mLines = savedInstanceState.getParcelableArrayList(EXTRA_LINES);
		}
		MyFeture feture = new MyFeture(pname, path, cname, ydgeoFeature, ydMyLayer);
		mAdapter = new YddcLineAdapter(YzlYddActivity.this, mLines,feture);
		if(mAdapter != null){
			listView.setAdapter(mAdapter);
		}

		mmdc = (TextView) findViewById(R.id.yddc_mmdcb);
		mmdc.setOnClickListener(new MyListener());

		yddcxbh = (TextView) findViewById(R.id.yddc_xbh);
		yddcxbh.setText(ydcurrentxbh);

		back = (TextView) findViewById(R.id.yddc_btnreturn);
		back.setOnClickListener(new MyListener());

		piclook = (TextView) findViewById(R.id.yddc_seepic);
		piclook.setOnClickListener(new MyListener());

		yddc_xbm = (TextView) findViewById(R.id.yddc_xbm);
		yddc_xbm.setOnClickListener(new MyListener());

		//gcmc = BussUtil.getConfigXml(mContext, pname,"wysbzd");// 项目名称
	}

	@Override
	public View getParentView() {
		return parentView;
	}

	class MyListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.yddc_btnreturn:
					/*返回按钮*/
					finishThis();
					break;
				case R.id.yddc_seepic:
					//图片浏览
					lookpictures(YzlYddActivity.this);
					break;
				case R.id.yddc_mmdcb:
					/*添加样木调查数据*/
					//addYmdcData();
					break;
				case R.id.yddc_xbm://跳转到小班属性页
					toXbFeature();
					break;
				default:
					break;
			}
		}
	}

	/** 绑定数据*/
	public ArrayList<Line> createLines() {
		ArrayList<Line> lines = new ArrayList<Line>();
		LINE_NUM = ydgeoFeature.getAttributes().size();
		String xianD = "",xiangD= "";
		for (int i = 0; i < LINE_NUM; i++) {
			Field field = ydd_fields.get(i);
			Line line = new Line();
			line.setNum(i);
			line.setTview(field.getAlias());
			line.setfLength(field.getLength());
			line.setKey(field.getName());
			CodedValueDomain domain = (CodedValueDomain) field.getDomain();
			line.setDomain(domain);
			line.setFieldType(field.getFieldType());
			boolean ff = field.isNullable();
			line.setNullable(ff);

			Object obj = ydgeoFeature.getAttributes().get(field.getName());
			if(obj != null){
				String value = obj.toString().trim();
				if(field.getAlias().contains("县") || field.getAlias().contains("XIAN") ||field.getAlias().contains("xian")){
					xianD = value;
				}else if(field.getAlias().contains("乡") || field.getAlias().contains("XIANG") ||field.getAlias().contains("xiang")){
					xiangD = value;
				}
				if(line.getFieldType() == Field.esriFieldTypeDate){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date(Long.parseLong(obj.toString()));
					value = sdf.format(date);
				}

				if(domain != null){
					Map<String, String> values = domain.getCodedValues();
					for(String key : values.keySet()){
						if(key.equals(value)){
							line.setText(values.get(key));
							break;
						}else if(field.getAlias().contains("乡") || field.getAlias().contains("XIANG") ||field.getAlias().contains("xianG")){
							if(key.equals(xianD+value)){
								line.setText(values.get(key));
								break;
							}
						}else if(field.getAlias().contains("村")|| field.getAlias().contains("CUN") ||field.getAlias().contains("cun")){
							if(key.equals(xiangD+value)){
								line.setText(values.get(key));
								break;
							}
						}else{
							line.setText(value);
						}
					}
				}else{
					Map<String, String> xianMap = Util.getXianValue(mContext);
					Map<String, String> xiangMap = Util.getXiangValue(mContext);
					Map<String, String> cunMap = Util.getCunValue(mContext);
					if(field.getAlias().contains("县") || field.getName().equals("xian") || field.getName().equals("XIAN")){
						xianD = value;
						String str = Util.getXXCValue(mContext, value, "", xianMap);
						line.setText(str);
					}else if(field.getAlias().contains("乡") || field.getName().equals("xiang") || field.getName().equals("XIANG")){
						xiangD = value;
						String str = Util.getXXCValue(mContext, value, xianD, xiangMap);
						line.setText(str);
					}else if(field.getAlias().contains("村") || field.getName().equals("cun") || field.getName().equals("CUN")){
						String str = value;
						if(xiangD.contains(xianD)){
							str = Util.getXXCValue(mContext, value, xiangD, cunMap);
						}else{
							str = Util.getXXCValue(mContext, value,xianD+xiangD, cunMap);
						}
						line.setText(str);
					}else{
						List<Row> list = isDMField(field.getName());
						if(list != null && list.size() > 0){
							for(Row row : list){
								if(row.getId().equals(value)){
									line.setText(row.getName());
									break;
								}else{
									line.setText(value);
								}
							}
						}else{
							line.setText(value);
						}
					}
				}
			}else{
				line.setText("");
			}
			lines.add(line);
		}
		return lines;
	}

	/**添加样地点 到数据图层*/
	public void addFeatureTolayer(Point point){
		Geometry geom = GeometryEngine.simplify(point,SpatialReference.create(2343));
		addFeatureOnLayer(geom, layerFeatureAts);
	}

	/** 添加feature在图层上 */
	public void addFeatureOnLayer(Geometry geom,Map<String, Object> selectFeatureAts) {
		try {

			GeodatabaseFeatureTable table = ydMyLayer.getTable();
			GeodatabaseFeature g = table.createFeatureWithTemplate(layerTemplate, geom);
			Symbol symbol = ydMyLayer.getRenderer().getSymbol(g);
			// symbol为null也可以 why？
			Map<String, Object> editAttributes = new HashMap<>();
			if (selectFeatureAts == null) {
				editAttributes.putAll(g.getAttributes());
			} else {
				editAttributes.putAll(selectFeatureAts);
			}
			//TODO
			@SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String str = format.format(new Date());
			editAttributes.put("WYBH", str);
			editAttributes.put("aaa",0);

			for(Field field : table.getFields()){
				String key = field.getName();
				int type = field.getFieldType();
				boolean flag = field.isNullable();
				Object value = editAttributes.get(key);
				if(!flag && value == null){
					if(type == Field.esriFieldTypeDouble){
						editAttributes.put(key,0.0);
					}else if(type == Field.esriFieldTypeDate){
						editAttributes.put(key,new Date());
					}else{
						editAttributes.put(key,"0");
					}
				}
			}

			Graphic addedGraphic = new Graphic(geom, symbol, editAttributes);
			long id = table.addFeature(addedGraphic);

			Feature feature = table.getFeature(id);
			Geometry geometry = feature.getGeometry();
			if (geometry.isEmpty() || !geometry.isValid()) {
				table.deleteFeature(id);
				feutureID = id;
			    ydgeoFeature = (GeodatabaseFeature) ydMyLayer.getLayer().getFeature(feutureID);
			    ydd_fields = ydgeoFeature.getTable().getFields();
			}
		} catch (TableException e) {
			e.printStackTrace();
		}

//		try {
//			GeodatabaseFeatureTable table = ydMyLayer.getTable();
//			GeodatabaseFeature g = table.createFeatureWithTemplate(layerTemplate, geom);
//			Symbol symbol = ydMyLayer.getRenderer().getSymbol(g);
//			// symbol为null也可以 why？
//			Map<String, Object> editAttributes = new HashMap<>();
//			if (selectFeatureAts == null) {
//				editAttributes.putAll(g.getAttributes());
//			} else {
//				editAttributes.putAll(selectFeatureAts);
//			}
//
//			for(Field field : table.getFields()){
//				String key = field.getName();
//				int type = field.getFieldType();
//				boolean flag = field.isNullable();
//				Object value = editAttributes.get(key);
//				if(!flag && value == null){
//					if(type == Field.esriFieldTypeDouble){
//						editAttributes.put(key,0.0);
//					}else if(type == Field.esriFieldTypeDate){
//						editAttributes.put(key,new Date());
//					}else{
//						editAttributes.put(key,"0");
//					}
//				}
//			}
//
//			//TODO
//			@SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
//			String str = format.format(new Date());
//			editAttributes.put("WYBH", str);
//
//			GeodatabaseFeature feature = new GeodatabaseFeature(editAttributes,geom,table);
//			List<Feature> features = new ArrayList<>();
//			features.add(feature);
//			Graphic addedGraphic = new Graphic(geom, symbol, editAttributes);
//			table.addFeatures(features);
//			//feutureID = id;
//			//ydgeoFeature = (GeodatabaseFeature) ydMyLayer.getLayer().getFeature(feutureID);
//			//ydd_fields = ydgeoFeature.getTable().getFields();
//		} catch (TableException e) {
//			e.printStackTrace();
//		}

	}

	/**更新样地点数据*/
	public void updataData(){
		Graphic updateGraphic = new Graphic(ydgeoFeature.getGeometry(),ydgeoFeature.getSymbol(),layerFeatureAts);
		GeodatabaseFeatureTable table = (GeodatabaseFeatureTable) ydMyLayer.getLayer().getFeatureTable();
		for(Field field : table.getFields()){
			String key = field.getName();
			int type = field.getFieldType();
			boolean flag = field.isNullable();
			Object value = layerFeatureAts.get(key);
			if(!flag && value == null){
				if(type == Field.esriFieldTypeDouble){
					layerFeatureAts.put(key,0.0);
				}else if(type == Field.esriFieldTypeDate){
					layerFeatureAts.put(key,new Date());
				}else{
					layerFeatureAts.put(key,"0");
				}
			}
		}
		try
		{
			GeodatabaseFeature feature = new GeodatabaseFeature(layerFeatureAts,table);
			table.updateFeature(ydid, feature);
		} catch (TableException e)
		{
			e.printStackTrace();
			ToastUtil.setToast((Activity) mContext, "更新失败");
			return;
		}
		try {
			ydgeoFeature = (GeodatabaseFeature) ydMyLayer.getTable().getFeature(ydid);
		} catch (TableException e) {
			e.printStackTrace();
		}
		//fieldList = geoFeature.getTable().getFields();
	}

	/** 获取图片保存地址 */
	public String getImagePath(String path) {
		File file = new File(path);
		String path1 = file.getParent()+ "/images";
		File file2 = new File(path1);
		boolean flag = file2.exists();
		if(!flag){
			file2.mkdirs();
		}
		if(ydcurrentxbh==null || ydcurrentxbh.equals("")){
			if(xbwybh != null || xbwybh.equals("")){
				String path2 = file2.getPath()+"/"+xbwybh;
				File file3 = new File(path2);
				if(!file3.exists()){
					file3.mkdirs();
				}
				picPath = file3.getPath();
			}else{
				picPath = path1;
			}

		}else{
			String path2 = file2.getPath()+"/"+ydcurrentxbh;
			File file3 = new File(path2);
			if(!file3.exists()){
				file3.mkdirs();
			}
			picPath = file3.getPath();
		}
		return picPath;
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
					layerRenderer = flayer.getRenderer();
					layerSymbol = layerRenderer.getSymbol(g);
					layerTemplate = featureTemplate;
					layerFeatureAts = g.getAttributes();
					ydd_fields = g.getTable().getFields();
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
						g = ((GeodatabaseFeatureTable) flayer.getFeatureTable()).createFeatureWithTemplate(featureTemplate,null);
						layerRenderer = flayer.getRenderer();
						layerSymbol = layerRenderer.getSymbol(g);
						layerTemplate = featureTemplate;
						layerFeatureAts = g.getAttributes();
						ydd_fields = g.getTable().getFields();
					} catch (TableException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
									Intent intent) {
		if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
			updateZPBH();
			dealPhotoFile(mCurrentPhotoPath);

		}
	}

	/**更新照片编号*/
	public void updateZp(String pctext,Line line,String bfText){
		picPath = getImagePath(path);
		this.pcLine = line;
		updateJjzp(pctext,line,bfText);
	}
	public void updateJjzp(String pctext,Line line,String bfText){
		GeodatabaseFeature feature = (GeodatabaseFeature) featureLayer.getFeature(ydgeoFeature.getId());
		Map<String, Object> geoFeatureAts = feature.getAttributes();

		if(pctext == null || (bfText != null && pctext.equals(bfText))){
			return;
		}

		boolean flag = true;
		int length = pctext.length();
		int size = 0;
		for(Field ff : ydd_fields){
			if(ff.getName().equals(line.getKey())){
				size = ff.getLength();
				break;
			}
		}

		if(length > size){
			ToastUtil.setToast(mContext, "数据长度超过数据库规定长度");
			return;
		}
		geoFeatureAts.put(line.getKey(), pctext);

		//Graphic updateGraphic = new Graphic(feature.getGeometry(),feature.getSymbol(),geoFeatureAts);
		GeodatabaseFeatureTable featureTable = (GeodatabaseFeatureTable) featureLayer.getFeatureTable();
		try
		{
			long featureid = feature.getId();
			GeodatabaseFeature geodatabaseFeature = new GeodatabaseFeature(geoFeatureAts, featureTable);
			featureTable.updateFeature(featureid, geodatabaseFeature);
		} catch (TableException e)
		{
			flag = false;
			e.printStackTrace();
		}
		if(flag){
			line.setText(pctext);
			mAdapter.notifyDataSetChanged();
			ToastUtil.setToast((Activity) mContext, "照片编号更新成功");
		}else{
			ToastUtil.setToast((Activity) mContext, "照片编号更新失败");
		}
	}



	@Override
	public void onBackPressed() {
		finishThis();
		return;
	}

	/**跳转至小班属性页*/
	public void toXbFeature(){
		this.finish();
	}

	private void getLayer(){
		for(MyLayer myLayer : BaseActivity.layerNameList){
			String name = myLayer.getCname();
			Type type = myLayer.getTable().getGeometryType();
			String lname = myLayer.getTable().getTableName();
			if(type.equals(Geometry.Type.POINT) && name.equals(xbmyFeture.getCname()) && lname.equals(yddLayername)){
				ydMyLayer = myLayer;
				getEditSymbo(ydMyLayer.getLayer());
				break;
			}
		}
	}



}
