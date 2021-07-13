package com.chaos.gray;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangxingqi
 * @since 2021/7/13
 */

public class GrayRule {
    // 从配置文件中加载的灰度规则
    private Map<String, IGrayFeature> darkFeatures = new HashMap<>();
    // 编程实现的灰度规则
    private ConcurrentHashMap<String, IGrayFeature> programmedDarkFeatures = new ConcurrentHashMap<>();

    public void addProgrammedDarkFeature(String featureKey, IGrayFeature darkFeature) {
        programmedDarkFeatures.put(featureKey, darkFeature);
    }

    public void setDarkFeatures(Map<String, IGrayFeature> newDarkFeatures) {
        this.darkFeatures = newDarkFeatures;
    }

    public IGrayFeature getDarkFeature(String featureKey) {
        IGrayFeature darkFeature = programmedDarkFeatures.get(featureKey);
        if (darkFeature != null) {
            return darkFeature;
        }
        return darkFeatures.get(featureKey);
    }
}