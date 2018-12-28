package com.titan.ycslzy.util;

/**
 * Created by otitan_li on 2018/1/9.
 * SpatiaProUtil
 */

public class SpatiaProUtil {

    public static com.esri.core.geometry.SpatialReference wgs_4326(){
        return com.esri.core.geometry.SpatialReference.create(4326);
    }

    public static com.esri.core.geometry.SpatialReference xian_2382(){
        return com.esri.core.geometry.SpatialReference.create(2382);
    }

}
