package com.chaos.gray;

import com.chaos.gray.core.IGrayFeature;

/**
 * @author huangxingqi
 * @since 2021/7/15
 */
public class MainTest {
    public static void main(String[] args) {
        GrayLaunch grayLaunch=new GrayLaunch();
        //grayLaunch.addProgrammedDarkFeature("user_promotion", new UserPromotionDarkRule()); // 添加编程实现的灰度规则
        //IGrayFeature darkFeature = grayLaunch.getDarkFeature("user_promotion");
        IGrayFeature grayFeature = grayLaunch.getGreyFeature("grey_user_id");
        System.out.println("grey rule enable:"+grayFeature.enabled());
        System.out.println("gray result:"+grayFeature.gray(11));
        System.out.println("gray result:"+grayFeature.gray(1));


        IGrayFeature deviceGrayFeature = grayLaunch.getGreyFeature("device_transfered_2.0");
        System.out.println(deviceGrayFeature.gray("deviceId-123"));
        System.out.println(deviceGrayFeature.gray("deviceId-test-1"));

    }
}
