package org.zywx.wbpalmstar.plugin.uexbaidumap;

/**
 * 自定义距离工具类
 * <p>
 * 最新SDK有，但是为了保证插件稳定，决定暂不升级SDK
 *
 * @author waka
 */
public class MyDistanceUtils {

    public static final double SMALL_DISTANCE_FLAG = 0.1;

    public static final double DEF_PI = 3.14159265359; // PI
    public static final double DEF_2PI = 6.28318530712; // 2*PI
    public static final double DEF_PI180 = 0.01745329252; // PI/180.0
    public static final double DEF_R = 6370693.5; // radius of earth

    /* 智能计算使用哪种方法计算距离 */
    public static double getDistance(double lon1, double lat1, double lon2, double lat2) {

        double distance = -1;

        // 判断，如果是小距离
        if ((Math.abs(lat1 - lat2) < MyDistanceUtils.SMALL_DISTANCE_FLAG)
                && (Math.abs(lon1 - lon2) < MyDistanceUtils.SMALL_DISTANCE_FLAG)) {
            distance = MyDistanceUtils.getShortDistance(lon1, lat1, lon2, lat2);
        }
        // 大距离
        else {
            distance = MyDistanceUtils.getLongDistance(lon1, lat1, lon2, lat2);
        }

        return distance;
    }

    /* 利用勾股定理计算，适用于两点距离很近的情况 */
    public static double getShortDistance(double lon1, double lat1, double lon2, double lat2) {
        double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 经度差
        dew = ew1 - ew2;
        // 若跨东经和西经180 度，进行调整
        if (dew > DEF_PI)
            dew = DEF_2PI - dew;
        else if (dew < -DEF_PI)
            dew = DEF_2PI + dew;
        dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
        dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
        // 勾股定理求斜边长
        distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
    }

    /* 按标准的球面大圆劣弧长度计算，适用于距离较远的情况 */
    public static double getLongDistance(double lon1, double lat1, double lon2, double lat2) {
        double ew1, ns1, ew2, ns2;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 求大圆劣弧与球心所夹的角(弧度)
        distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
        // 调整到[-1..1]范围内，避免溢出
        if (distance > 1.0)
            distance = 1.0;
        else if (distance < -1.0)
            distance = -1.0;
        // 求大圆劣弧长度
        distance = DEF_R * Math.acos(distance);
        return distance;
    }
}
