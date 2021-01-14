package com.charlie;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicTest{

    private volatile Integer count1 = 0;
    private AtomicInteger count2 = new AtomicInteger(0);

    private void method1() {
        for (int i=0; i<1000; i++) {
            int a = i;
        }
        synchronized (count1){
            count1++;
        }
    }    
    private void method2() {
        for (int i=0; i<1000; i++) {
            int a = i;
        }
        count2.getAndIncrement();
    }

    public class Thread1 extends Thread{
        public Date start = null;
        public Date stop = null;
        @Override
        public void run() {
            start = new Date();
            for(int i=0;i<1000000;i++) {
                method1();
            }
            stop = new Date();
        } 
    }    
    public class Thread3 extends Thread{
        public Date start = null;
        public Date stop = null;
        @Override
        public void run() {
            start = new Date();
            for(int i=0;i<1000000;i++) {
                method2();
            }
            stop = new Date();
        } 
    }    
    static Thread1 t1 = null;
    static Thread1 t2 = null;
    static Thread3 t3 = null;
    static Thread3 t4 = null;
    static AtomicTest master = null;

    public static void main(String[] args) {
        AtomicTest master = new AtomicTest();
        t1 = master.new Thread1();
        t2 = master.new Thread1();
        t3 = master.new Thread3();
        t4 = master.new Thread3();
        t1.start();t2.start();t3.start();t4.start();
        Boolean finished = false;
        while(!finished) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(t1.stop != null && t2.stop != null && t3.stop != null && t4.stop != null) {
                Long sum1 = (t1.stop.getTime() - t1.start.getTime()) + (t2.stop.getTime() - t2.start.getTime());
                Long sum2 = (t3.stop.getTime() - t3.start.getTime()) + (t4.stop.getTime() - t4.start.getTime());
                finished = true;
                System.out.println("sum1 =" + sum1 + "ms sum2 =" + sum2 + "ms");
                System.out.println("count1 =" + master.count1 + " count2 =" + master.count2);
            }
        }    
    }
}
