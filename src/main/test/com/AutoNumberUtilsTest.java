package com;

import org.framework.core.utils.OrderNumberUtils;

/**
 * Created by User on 2017/6/27.
 */
public class AutoNumberUtilsTest {

    public static void main(String[] args) {
        TestRunnable testRunnable01 = new TestRunnable();
        TestRunnable testRunnable02 = new TestRunnable();
        TestRunnable testRunnable03 = new TestRunnable();
        TestRunnable testRunnable04 = new TestRunnable();
        TestRunnable testRunnable05 = new TestRunnable();
        testRunnable01.start();
        testRunnable02.start();
        testRunnable03.start();
        testRunnable04.start();
        testRunnable05.start();
    }
}

class TestRunnable extends Thread{
    public void run() {
        while (true){
            System.out.println(OrderNumberUtils.generateOrderNumber());
        }
    }
}
