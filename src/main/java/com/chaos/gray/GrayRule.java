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
    private Map<String, IGrayFeature> greyFeatures = new HashMap<>();
    // 编程实现的灰度规则
    private ConcurrentHashMap<String, IGrayFeature> fixedGreyFeatures = new ConcurrentHashMap<>();

    /**
     * 写代码新增灰度规则
     * @param featureKey
     * @param darkFeature
     */
    public void addFixedGreyFeature(String featureKey, IGrayFeature darkFeature) {
        fixedGreyFeatures.put(featureKey, darkFeature);
    }

    /**
     * 设置灰度规则列表
     * @param newDarkFeatures
     */
    public void setGreyFeatures(Map<String, IGrayFeature> newDarkFeatures) {
        this.greyFeatures = newDarkFeatures;
    }

    /**
     * 根据规则key获取灰度处理实例
     * @param featureKey
     * @return
     */
    public IGrayFeature getGreyFeature(String featureKey) {
        //fixed优先级较高
        IGrayFeature darkFeature = fixedGreyFeatures.get(featureKey);
        if (darkFeature != null) {
            return darkFeature;
        }
        return greyFeatures.get(featureKey);
    }
}