package com.chaos.gray;

import com.chaos.gray.model.GrayRuleConfig;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

/**
 * @author huangxingqi
 * @since 2021/7/13
 */

public class GrayFeature implements IGrayFeature{
    private String key;
    private boolean enabled;
    private int percentage;
    private RangeSet<Long> rangeSet = TreeRangeSet.create();

    public GrayFeature(GrayRuleConfig.GrayFeatureConfig grayFeatureConfig) {
        this.key = grayFeatureConfig.getKey();
        this.enabled = grayFeatureConfig.isEnabled();
        String darkRule = grayFeatureConfig.getRule().trim();
        parseGrayRule(darkRule);
    }

    /**
     * 解析灰度规则字符串
     * @param darkRule
     */
    protected void parseGrayRule(String darkRule) {
        if (!darkRule.startsWith("{") || !darkRule.endsWith("}")) {
            throw new RuntimeException("Failed to parse dark rule: " + darkRule);
        }

        String[] rules = darkRule.substring(1, darkRule.length() - 1).split(",");
        this.rangeSet.clear();
        this.percentage = 0;
        for (String rule : rules) {
            if (rule==null||"".equals(rule)) {
                continue;
            }
            rule = rule.trim();

            if (rule.startsWith("%")) {
                int newPercentage = Integer.parseInt(rule.substring(1));
                if (newPercentage > this.percentage) {
                    this.percentage = newPercentage;
                }
            } else if (rule.contains("-")) {
                String[] parts = rule.split("-");
                if (parts.length != 2) {
                    throw new RuntimeException("Failed to parse dark rule: " + darkRule);
                }
                long start = Long.parseLong(parts[0]);
                long end = Long.parseLong(parts[1]);
                if (start > end) {
                    throw new RuntimeException("Failed to parse dark rule: " + darkRule);
                }
                this.rangeSet.add(Range.closed(start, end));
            } else {
                long val = Long.parseLong(rule);
                this.rangeSet.add(Range.closed(val, val));
            }
        }
    }

    @Override
    public boolean enabled() {
        return this.enabled;
    }

    /**
     * 灰度判断
     * @param darkTarget
     * @return
     */
    @Override
    public boolean gray(long darkTarget) {
        boolean selected = this.rangeSet.contains(darkTarget);
        if (selected) {
            return true;
        }

        long reminder = darkTarget % 100;
        if (reminder >= 0 && reminder < this.percentage) {
            return true;
        }

        return false;
    }

    @Override
    public boolean gray(String darkTarget) {
        long target = Long.parseLong(darkTarget);
        return gray(target);
    }
}