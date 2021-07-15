package com.chaos.gray.model;

import lombok.Data;

import java.util.List;

/**
 * @author huangxingqi
 * @since 2021/7/13
 */

public class GrayRuleConfig {
    private List<GrayFeatureConfig> features;

    public List<GrayFeatureConfig> getFeatures() {
        return this.features;
    }

    public void setFeatures(List<GrayFeatureConfig> features) {
        this.features = features;
    }


    @Data
    public static class GrayFeatureConfig {
        private String key;
        private boolean enabled;
        private String rule;
        private String dataType;

    }
}
