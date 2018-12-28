package com.titan.ycslzy.util;

import android.content.Context;

import com.google.gson.Gson;
import com.titan.ycslzy.MyApplication;
import com.titan.ycslzy.db.DataBaseHelper;
import com.titan.ycslzy.entity.Gjpoint;
import com.titan.ycslzy.service.Webservice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by li on 2017/9/25.
 * 连接网络后，上传轨迹数据线程类
 */

public class TrajectoryThreed extends Thread{

    Context context;

    public TrajectoryThreed(Context context){
        this.context = context;
    }

    @Override
    public void run() {
        super.run();
        //获取本地在特定时间内没有上传的轨迹是点
        List<Gjpoint> gjpoints = getNotuploadedPoint();
        if(gjpoints.size() > 0){
            //上传未上传的轨迹点数据
            uploadPoint(gjpoints);
            //更新未上传的轨迹点数据
            updataNotUploadPoint(gjpoints);
        }
    }

    /**
     * //获取本地在特定时间内没有上传的轨迹是点
     * get not uploaded guiji point
     */
    private List<Gjpoint> getNotuploadedPoint(){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String endtime = dateFormat.format(date);
        Date bfdate = getBeforeDay(date);
        String starttime = dateFormat.format(bfdate);

        List<Gjpoint> gjpoints = DataBaseHelper.getNotUploadedPoint(context, MyApplication.macAddress,starttime,endtime);
        return gjpoints;
    }

    /**
     * //上传未上传的轨迹点数据
     * up load guji point to server
     */
    private void uploadPoint(List<Gjpoint> gjpoints){
        Gson gson = new Gson();
        gson.toJson(gjpoints);
        //数据通过json传递到后台，后台进行json数据解析入库
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Webservice webservice = new Webservice(context);
        for (Gjpoint gjpoint : gjpoints){
            webservice.upPoint(MyApplication.macAddress,gjpoint.getLon(), gjpoint.getLat(),
                    dateFormat.format(gjpoint.getTime()), MyApplication.mobileXlh,MyApplication.mobileType);
        }
    }

    /**
     * //更新未上传的轨迹点数据
     * updata not upload point
     */
    private void updataNotUploadPoint(List<Gjpoint> gjpoints){
        DataBaseHelper.updataPoint(context,gjpoints);
    }

    /**
     * 获取前五天
     * get before day
     */
    private static Date getBeforeDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -5);
        date = calendar.getTime();
        return date;
    }

}
