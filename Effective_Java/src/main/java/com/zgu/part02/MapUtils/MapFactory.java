package com.zgu.part02.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhugu
 * 2019/9/29 21:55
 */
public class MapFactory {
    public static <K, V>HashMap<K, V> newInstance() {
        return new HashMap<K, V>();
    }

    public static void main(String[] args) {
        Map<String, List<String>> map = MapFactory.newInstance();
    }
}
