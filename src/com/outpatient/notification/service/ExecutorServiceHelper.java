/*
 * File Name: com.huawei.ame.framework.ExecutorServiceHelper.java
 *
 * Copyright Notice:
 *      Copyright  1998-2007, Huawei Technologies Co., Ltd.  ALL Rights Reserved.
 *
 *      Warning: This computer software sourcecode is protected by copyright law
 *      and international treaties. Unauthorized reproduction or distribution
 *      of this sourcecode, or any portion of it, may result in severe civil and
 *      criminal penalties, and will be prosecuted to the maximum extent
 *      possible under the law.
 */
package com.outpatient.notification.service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.util.Log;


/**
 * 任务服务的帮助类
 * 
 * <p>
 * schedule 方法使用各种延迟创建任务，并返回一个可用于取消或检查执行的任务对象。 scheduleAtFixedRate
 * 方法创建并执行某些在取消前一直定期运行的任务。
 * @author j66969 Create on 2011-4-1
 * @see
 * @since 1.0
 */
public abstract class ExecutorServiceHelper
{
    /**
     * 日志器
     */
    private static final String TAG = ExecutorServiceHelper.class.getSimpleName();

    private static final int CORE_POOLSIZE = 10;

    /**
     * 调度执行服务线程池
     */
    private static ScheduledExecutorService scheduledExecutorServicePool;

    static
    {
        scheduledExecutorServicePool = Executors
                .newScheduledThreadPool(CORE_POOLSIZE);

        if (Log.isLoggable(TAG, Log.DEBUG))
        {
            Log.d(TAG,"Create scheduled thread pool success, core pool size = "
                    + CORE_POOLSIZE);
        }
    }

    /**
     * 提交一个 Runnable 任务用于执行，并返回一个表示该任务的 Future。
     * @param command 要执行的任务
     * @return 表示任务等待完成的 Future，并且其 get() 方法在完成后将返回 null。
     */
    @SuppressWarnings("rawtypes")
    public static Future schedule(Runnable command)
    {
        return scheduledExecutorServicePool.submit(command);
    }

    /**
     * 创建并执行在给定延迟后启用的一次性操作。
     * @param command 要执行的任务
     * @param delay 从现在开始延迟执行的时间
     * @param unit 延迟参数的时间单位
     * @return 可用于提取结果或取消的 ScheduledFuture
     */
    public static ScheduledFuture<?> schedule(Runnable command, long delay,
            TimeUnit unit)
    {
        return scheduledExecutorServicePool.schedule(command, delay, unit);
    }

    /**
     * 创建并执行一个在给定初始延迟后首次启用的定期操作，后续操作具有给定的周期；也就是将在 initialDelay 后开始执行， 然后在
     * initialDelay+period 后执行，接着在 initialDelay + 2 * period 后执行，依此类推。如果任务的任一
     * 执行遇到异常，都会取消后续执行。否则，只能通过执行程序的取消或终止方法来终止该任务
     * @param command 要执行的任务
     * @param initialDelay 首次执行的延迟时间
     * @param period 连续执行之间的周期
     * @param unit 延迟参数的时间单位
     * @return 可用于提取结果或取消的 ScheduledFuture
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
            long initialDelay, long period, TimeUnit unit)
    {
        return scheduledExecutorServicePool.scheduleAtFixedRate(command,
                initialDelay, period, unit);
    }

    /**
     * 创建并执行一个在给定初始延迟后首次启用的定期操作，后续操作具有给定的周期；也就是将在 initialDelay 后开始执行， 然后在
     * 每次任务执行完成后延迟delay时间后执行，即再次执行时间固定时间为delay。如果任务的任一
     * 执行遇到异常，都会取消后续执行。否则，只能通过执行程序的取消或终止方法来终止该任务
     * @param command 要执行的任务
     * @param initialDelay 首次执行的延迟时间
     * @param delay 两次执行之间的时间间隔
     * @param unit 延迟参数的时间单位
     * @return 可用于提取结果或取消的 ScheduledFuture
     */
    public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
            long initialDelay, long delay, TimeUnit unit)
    {
        return scheduledExecutorServicePool.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
 
    
    
    /**
     * 启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用。
     */
    protected static void shutdown()
    {
        if (Log.isLoggable(TAG, Log.DEBUG))
        {
            Log.d(TAG,"Shutdown scheduled thread pool.");
        }
        scheduledExecutorServicePool.shutdown();
    }

    /**
     * 试图停止所有正在执行的活动任务，暂停处理正在等待的任务，并返回等待执行的任务列表。
     * 无法保证能够停止正在处理的活动执行任务，但是会尽力尝试。例如，通过 Thread.interrupt()
     * 来取消典型的实现，所以如果任何任务屏蔽或无法响应中断，则可能永远无法终止该任务。
     * @return 从未开始执行的任务的列表。
     */
    public static List<Runnable> shutdownNow()
    {
        if (Log.isLoggable(TAG, Log.DEBUG))
        {
            Log.d(TAG,"Force shutdown scheduled thread pool.");
        }
        return scheduledExecutorServicePool.shutdownNow();
    }
}
