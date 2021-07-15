package com.chaos.gray.core;

import com.chaos.gray.model.GrayRuleConfig;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

import javax.xml.stream.FactoryConfigurationError;
import java.util.HashSet;
import java.util.Set;

/**
 * @author huangxingqi
 * @since 2021/7/13
 */

public class GrayFeatureByConfigImpl implements IGrayFeature{
    private String key;
    private boolean enabled;
    private int percentage;

    //利用RangeSet来实现数字类型的灰度
    private RangeSet<Long> rangeSet ;

    private Set<String> greyStringSet;

    public GrayFeatureByConfigImpl(GrayRuleConfig.GrayFeatureConfig grayFeatureConfig) {
        this.key = grayFeatureConfig.getKey();
        this.enabled = grayFeatureConfig.isEnabled();
        String darkRule = grayFeatureConfig.getRule().trim();

        switch (grayFeatureConfig.getDataType()){
            case "Long":
                rangeSet = TreeRangeSet.create();
                parseGrayRule(darkRule);
                break;
            case "String":
                greyStringSet=new HashSet<>();
                parseStringGrayRule(darkRule);
                break;
        }

    }

    protected void parseStringGrayRule(String greyRule){
        String[] rules=greyRule.split(",");
        if(rules==null||rules.length<1){
            return;
        }

        greyStringSet.clear();
        for(String rule:rules){
            greyStringSet.add(rule);
        }

    }

    /**
     * 解析灰度规则字符串
     * @param greyRule
     */
    protected void parseGrayRule(String greyRule) {
        /*if (!darkRule.startsWith("{") || !darkRule.endsWith("}")) {
            throw new RuntimeException("Failed to parse dark rule: " + darkRule);
        }*/

        //String[] rules = darkRule.substring(1, darkRule.length() - 1).split(",");
        String[] rules = greyRule.split(",");
        this.rangeSet.clear();
        //this.percentage = 0;
        for (String rule : rules) {
            if (rule==null||"".equals(rule)) {
                continue;
            }
            rule = rule.trim();

            if (rule.startsWith("%")) {
                //百分比灰度
                int newPercentage = Integer.parseInt(rule.substring(1));
                if (newPercentage > this.percentage) {
                    this.percentage = newPercentage;
                }
            } else if (rule.contains("-")) {
                //范围灰度
                String[] parts = rule.split("-");
                if (parts.length != 2) {
                    throw new RuntimeException("Failed to parse dark rule: " + greyRule);
                }
                long start = Long.parseLong(parts[0]);
                long end = Long.parseLong(parts[1]);
                if (start > end) {
                    throw new RuntimeException("Failed to parse dark rule: " + greyRule);
                }
                this.rangeSet.add(Range.closed(start, end));
            } else {
                //精确灰度
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
     * 数字类型的灰度判断
     * @param darkTarget
     * @return
     */
    @Override
    public boolean gray(long darkTarget) {
        if(!enabled){
            //开关关闭 不灰度
            return false;
        }

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

    /**
     * 字符串类型的判断
     * @param darkTarget
     * @return
     */
    @Override
    public boolean gray(String darkTarget) {
        return greyStringSet.contains(darkTarget);
    }
}
