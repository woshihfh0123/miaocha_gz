package com.bs.xyplibs.utils;

import java.util.Map;

/**
 * Created by Administrator on 2018/11/29.
 *  Map相关操作
 */

public class MapUtils {
    private static final String DELIMITER = ",";

    /**
     * Judge whether a map is null or size is 0
     * 判断是否为空或者为零
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return (map == null || map.size() == 0);
    }

    /**
     * Map遍历
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> String traverseMap(Map<K, V> map) {
        if (!isEmpty(map)) {
            int len = map.size();
            StringBuilder builder = new StringBuilder(len);
            int i = 0;
            for (Map.Entry<K, V> entry : map.entrySet()) {
                if (entry == null) {
                    continue;
                }
                builder.append(entry.getKey().toString() + ":" + entry.getValue().toString());
                i++;
                if (i < len) {
                    builder.append(DELIMITER);
                }
            }
            return builder.toString();
        }
        return null;
    }

    /**
     * 根据值获取键
     *
     * @param map
     * @param value
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        if (isEmpty(map)) {
            return null;
        }
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (ObjectUtils.equals(entry.getValue(), value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
