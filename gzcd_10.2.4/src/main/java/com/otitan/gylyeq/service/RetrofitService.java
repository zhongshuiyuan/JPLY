package com.otitan.gylyeq.service;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by li on 2017/5/5.
 * 接口 retrofitservice 连接后台数据库
 */

public interface RetrofitService {

    @GET("/gyly/Service/Service.asmx/addMoblieSysInfo")
    Observable<String> addMoblieSysInfo(@Query("sysname") String sysname, @Query("tel") String tel,@Query("dw") String dw, @Query("retime") String retime,@Query("sbmc") String sbmc, @Query("sbh") String sbh,@Query("bz") String bz);

    @GET("/gyly/Service/Service.asmx/selSBUserInfo")
    Observable<String> selMobileSysInfo(@Query("sbh") String sbh,@Query("xlh") String xlh,@Query("sbmc") String type);
    /*提交疫源疫病信息*/
    @GET("/gyly/Service/Service.asmx/addYyybJcdwData")
    Observable<String> addYyybJcdwData(@Query("json") String json);
    /*获取监测单位数据*/
    @GET("/gyly/Service/Service.asmx/getYyybJcdwData")
    Observable<String> getJcdwData();
    /*分页获取运输台账数据*/
    @GET("/gyly/Service/Service.asmx/getYszInfo")
    Observable<String> getYszData(@Query("pageIndex") String pageIndex);

    /*木材经营 许可证查询*/
    @GET("/gyly/Service/Service.asmx/getXkznsInfo")
    Observable<String> getXkzData(@Query("id") int id);

    /*木材经营 企业信息添加*/
    @GET("/gyly/Service/Service.asmx/addQyInfo")
    Observable<String> addQyData(@Query("json") String json);
}
