package com.titan.ycslzy.service;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by li on 2017/5/5.
 * 接口
 */

public interface RetrofitService {

    @GET("/GYLYEQ/Service/Service.asmx/addMoblieSysInfo")
    Observable<String> addMoblieSysInfo(@Query("json") String json);

    @GET("/GYLYEQ/Service/Service.asmx/selSBUserInfo")
    Observable<String> selMobileSysInfo(@Query("sbh") String sbh, @Query("xlh") String xlh, @Query("sbmc") String type);

    @GET("/GYLYEQ/Service/Service.asmx/SelMobile")
    Observable<String> selMobile(@Query("SBH") String SBH);

    @GET("/GYLYEQ/Service/Service.asmx/UPPatrolEvent")
    Observable<String> addXjsjInfo(@Query("jsonText") String json);

}
