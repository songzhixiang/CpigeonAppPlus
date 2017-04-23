package com.cpigeon.app;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        long beginTime = System.currentTimeMillis() / 1000 - 60 * 60 * 24 * 3;
        System.out.println(beginTime + "");
    }
}