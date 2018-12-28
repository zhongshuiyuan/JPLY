package com.otitan.gylyeq.util;

import com.esri.core.geometry.SpatialReference;

/**
 * Created by otitan_li on 2018/5/15.
 * SpatialUtil
 */

public class SpatialUtil {

    public static SpatialReference defalutSpatialReference(){
        return SpatialReference.create(2343);
    }

}
