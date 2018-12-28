package com.titan.ycslzy.service;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by li on 2017/5/5.
 * 接口
 */

public interface RetrofitService {

    @GET("ycly/FireService/YclyWebService.asmx/updateName")
    Observable<String> addMoblieSysInfo(@Query("name") String sysname, @Query("sbh") String sbh, @Query("tel") String tel);
    @POST("ycly/FireService/YclyWebService.asmx/selMobileInfo")
    Observable<String> selMobileSysInfo(@Query("sbh") String sbh);
    /*系统运行时进行设备号和序列号入库*/
    @GET("ycly/FireService/YclyWebService.asmx/addMacAddress")
    Observable<String> addMacAddress(@Query("sbh") String sbh, @Query("xlh") String xlh);
    /*检查用户名和手机号是否录入*/
    @GET("ycly/FireService/YclyWebService.asmx/checkMobileUser")
    Observable<String> checkMobileUser(@Query("sbh") String sbh);
    /*实时传送GPS坐标*/
    @GET("ycly/FireService/YclyWebService.asmx/uPLonLat")
    Observable<String> uPLonLat(@Query("SBH") String sbh,@Query("LON") String LON,@Query("LAT") String LAT,@Query("time")String time);
    /*更新设备使用者信息*/
    @GET("ycly/FireService/YclyWebService.asmx/updateName")
    Observable<String> updateName(@Query("name") String name,@Query("SBH") String sbh,@Query("tel") String tel);
    /*检查登录*/
    @GET("ycly/FireService/YclyWebService.asmx/checkUser")
    Observable<String> checkUser(@Query("name") String username,@Query("psw") String password);


}
