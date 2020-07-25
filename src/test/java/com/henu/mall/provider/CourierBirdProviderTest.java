package com.henu.mall.provider;

import com.henu.mall.MallApplicationTests;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-04-12 17:19
 */
public class CourierBirdProviderTest extends MallApplicationTests {

    @Resource
    private CourierBirdProvider provider;
    @Test
    public void getOrderTracesByJson() {
        String expCode="STO";
        String expNo ="773028494163168";
        try {
            String result = provider.getOrderTracesByJson(expCode, expNo);

            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}