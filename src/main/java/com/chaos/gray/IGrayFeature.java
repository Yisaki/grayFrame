package com.chaos.gray;

/**
 * @author huangxingqi
 * @since 2021/7/13
 */
public interface IGrayFeature {
    boolean enabled();
    boolean gray(long darkTarget);
    boolean gray(String darkTarget);
}
