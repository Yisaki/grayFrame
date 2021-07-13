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


    public static class GrayFeatureConfig {
        private String key;
        private boolean enabled;
        private String rule;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }
    }
}
