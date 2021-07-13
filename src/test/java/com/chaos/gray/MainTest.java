package com.chaos.gray;

/**
 * @author huangxingqi
 * @since 2021/7/13
 */
public class MainTest {
    public static void main(String[] args) {
        GrayLaunch grayLaunch=new GrayLaunch();
        //grayLaunch.addProgrammedDarkFeature("user_promotion", new UserPromotionDarkRule()); // 添加编程实现的灰度规则
        //IGrayFeature darkFeature = grayLaunch.getDarkFeature("user_promotion");
        IGrayFeature darkFeature = grayLaunch.getDarkFeature("call_newapi_getUserById");
        System.out.println(darkFeature.enabled());
        System.out.println("gray result:"+darkFeature.gray(893));
        System.out.println("gray result:"+darkFeature.gray(1));
    }
}
