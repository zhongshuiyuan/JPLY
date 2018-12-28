package com.otitan.gylyeq.dao;

import android.content.Context;

import com.baidu.location.LocationClient;
import com.otitan.gylyeq.BaseActivity;

/**
 * Created by li on 2017/3/14.
 * 定位接口
 */

public interface ILocationDao {

    void initLocation(Context context, LocationClient client, BaseActivity.MyLocationListenner listenner);

}
