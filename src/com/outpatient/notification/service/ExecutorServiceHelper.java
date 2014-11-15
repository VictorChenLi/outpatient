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
 * �������İ�����
 * 
 * <p>
 * schedule ����ʹ�ø����ӳٴ������񣬲�����һ��������ȡ������ִ�е�������� scheduleAtFixedRate
 * ����������ִ��ĳЩ��ȡ��ǰһֱ�������е�����
 * @author j66969 Create on 2011-4-1
 * @see
 * @since 1.0
 */
public abstract class ExecutorServiceHelper
{
    /**
     * ��־��
     */
    private static final String TAG = ExecutorServiceHelper.class.getSimpleName();

    private static final int CORE_POOLSIZE = 10;

    /**
     * ����ִ�з����̳߳�
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
     * �ύһ�� Runnable ��������ִ�У�������һ����ʾ������� Future��
     * @param command Ҫִ�е�����
     * @return ��ʾ����ȴ���ɵ� Future�������� get() ��������ɺ󽫷��� null��
     */
    @SuppressWarnings("rawtypes")
    public static Future schedule(Runnable command)
    {
        return scheduledExecutorServicePool.submit(command);
    }

    /**
     * ������ִ���ڸ����ӳٺ����õ�һ���Բ�����
     * @param command Ҫִ�е�����
     * @param delay �����ڿ�ʼ�ӳ�ִ�е�ʱ��
     * @param unit �ӳٲ�����ʱ�䵥λ
     * @return ��������ȡ�����ȡ���� ScheduledFuture
     */
    public static ScheduledFuture<?> schedule(Runnable command, long delay,
            TimeUnit unit)
    {
        return scheduledExecutorServicePool.schedule(command, delay, unit);
    }

    /**
     * ������ִ��һ���ڸ�����ʼ�ӳٺ��״����õĶ��ڲ����������������и��������ڣ�Ҳ���ǽ��� initialDelay ��ʼִ�У� Ȼ����
     * initialDelay+period ��ִ�У������� initialDelay + 2 * period ��ִ�У��������ơ�����������һ
     * ִ�������쳣������ȡ������ִ�С�����ֻ��ͨ��ִ�г����ȡ������ֹ��������ֹ������
     * @param command Ҫִ�е�����
     * @param initialDelay �״�ִ�е��ӳ�ʱ��
     * @param period ����ִ��֮�������
     * @param unit �ӳٲ�����ʱ�䵥λ
     * @return ��������ȡ�����ȡ���� ScheduledFuture
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
            long initialDelay, long period, TimeUnit unit)
    {
        return scheduledExecutorServicePool.scheduleAtFixedRate(command,
                initialDelay, period, unit);
    }

    /**
     * ������ִ��һ���ڸ�����ʼ�ӳٺ��״����õĶ��ڲ����������������и��������ڣ�Ҳ���ǽ��� initialDelay ��ʼִ�У� Ȼ����
     * ÿ������ִ����ɺ��ӳ�delayʱ���ִ�У����ٴ�ִ��ʱ��̶�ʱ��Ϊdelay������������һ
     * ִ�������쳣������ȡ������ִ�С�����ֻ��ͨ��ִ�г����ȡ������ֹ��������ֹ������
     * @param command Ҫִ�е�����
     * @param initialDelay �״�ִ�е��ӳ�ʱ��
     * @param delay ����ִ��֮���ʱ����
     * @param unit �ӳٲ�����ʱ�䵥λ
     * @return ��������ȡ�����ȡ���� ScheduledFuture
     */
    public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
            long initialDelay, long delay, TimeUnit unit)
    {
        return scheduledExecutorServicePool.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
 
    
    
    /**
     * ����һ��˳��رգ�ִ����ǰ�ύ�����񣬵�����������������Ѿ��رգ������û���������á�
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
     * ��ͼֹͣ��������ִ�еĻ������ͣ�������ڵȴ������񣬲����صȴ�ִ�е������б�
     * �޷���֤�ܹ�ֹͣ���ڴ���Ļִ�����񣬵��ǻᾡ�����ԡ����磬ͨ�� Thread.interrupt()
     * ��ȡ�����͵�ʵ�֣���������κ��������λ��޷���Ӧ�жϣ��������Զ�޷���ֹ������
     * @return ��δ��ʼִ�е�������б�
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
