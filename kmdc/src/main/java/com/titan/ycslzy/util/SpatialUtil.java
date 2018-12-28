package com.titan.ycslzy.util;

import com.esri.core.geometry.SpatialReference;

/**
 * Created by otitan_li on 2018/6/11.
 * SpatialUtil
 */

public class SpatialUtil {

    public static SpatialReference defalutSpatialReference(){
        return SpatialReference.create(2343);
    }

}
