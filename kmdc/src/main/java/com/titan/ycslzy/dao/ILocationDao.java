package com.titan.ycslzy.dao;

import android.content.Context;

import com.baidu.location.LocationClient;
import com.titan.ycslzy.BaseActivity;

/**
 * Created by li on 2017/3/14.
 */

public interface ILocationDao {

    void initLocation(Context context, LocationClient client, BaseActivity.MyLocationListenner listenner);

}
