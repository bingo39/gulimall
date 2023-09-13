package com.atguigu.gulimall.search.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 实现线程的四种方法
 * 1）继承Thread
 * 2)实现Runnable接口
 * 3)实现Callable接口 + FutureTask(可以拿到返回结果，可以处理异常)
 * 4)线程池
 * <p>
 * 区别：
 * 1、2不能得到返回值
 * 1、2、3都不能控制资源。4系统是稳定
 */

public class ThreadTest {
    //创建线程池
    public static ExecutorService execuyor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main......start.....");
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
            return i;
        }, execuyor).whenComplete((result, eer) -> {
            System.out.println("异常任务成功完成...结果是:" + result + "异常是：" + eer);
        }).exceptionally(throwable -> {
            //感知异常，同时返回默认值
            return 10;
        });
        System.out.println("main.....end...." + future.get());

    }
}
