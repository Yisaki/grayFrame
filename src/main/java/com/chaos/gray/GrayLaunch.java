package com.chaos.gray;

import com.chaos.gray.model.GrayRuleConfig;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author huangxingqi
 * @since 2021/7/13
 */
//@Slf4j
public class GrayLaunch {

    private static final int DEFAULT_RULE_UPDATE_TIME_INTERVAL = 60; // in seconds
    private GrayRule rule = new GrayRule();
    private ScheduledExecutorService executor;

    public GrayLaunch(int ruleUpdateTimeInterval) {
        loadRule();
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                loadRule();
            }
        }, ruleUpdateTimeInterval, ruleUpdateTimeInterval, TimeUnit.SECONDS);
    }

    public GrayLaunch() {
        this(DEFAULT_RULE_UPDATE_TIME_INTERVAL);
    }

    private void loadRule() {
        //yaml->dto
        InputStream in = null;
        GrayRuleConfig ruleConfig = null;
        try {
            in = this.getClass().getResourceAsStream("/gray-rule.yaml");
            if (in != null) {
                Yaml yaml = new Yaml();
                ruleConfig = yaml.loadAs(in, GrayRuleConfig.class);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //log.error("close file error:{}", e);
                }
            }
        }

        if (ruleConfig == null) {
            throw new RuntimeException("Can not load dark rule.");
        }
        //yaml->dto end

        // 修改：单独更新从配置文件中得到的灰度规则，不覆盖编程实现的灰度规则
        Map<String, IGrayFeature> darkFeatures = new HashMap<>();
        List<GrayRuleConfig.GrayFeatureConfig> darkFeatureConfigs = ruleConfig.getFeatures();
        for (GrayRuleConfig.GrayFeatureConfig darkFeatureConfig : darkFeatureConfigs) {
            darkFeatures.put(darkFeatureConfig.getKey(), new GrayFeature(darkFeatureConfig));
        }
        this.rule.setDarkFeatures(darkFeatures);
    }

    // 新增：添加编程实现的灰度规则的接口
    public void addProgrammedDarkFeature(String featureKey, IGrayFeature darkFeature) {
        this.rule.addProgrammedDarkFeature(featureKey, darkFeature);
    }

    public IGrayFeature getDarkFeature(String featureKey) {
        IGrayFeature darkFeature = this.rule.getDarkFeature(featureKey);
        return darkFeature;
    }
}
