package com.jthinking.monitor;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * 内存监控器
 * @author JiaBochao
 * @version 2017-11-17 10:00:02
 */
public class MemoryListener {

    //Zookeeper地址
    private static String connectString = "192.168.1.111:2181";
    //Zookeeper连接超时时间
    private static int sessionTimeout = 5000;
    //Zookeeper节点基础路径
    private static String basePath = "/linux-server";
    //内存使用率计算时间间隔
    private static int timeInterval = 3000;
    //监控线程退出标志位
    private static boolean exit = false;

    /**
     * 开启内存监控
     */
    public static void startListen() {

        /**
         * 启动后向Zookeeper注册本机的IP以及内存使用率
         * 每隔timeInterval秒获取一次内存使用率
         * 程序停止后Zookeeper会自动将包含这些信息的节点删除
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                ZooKeeper zooKeeper = getZooKeeperConnection();
                String path = null;
                try {
                    path = basePath + "/" + SystemUtils.getLocalHostName();
                    // 临时节点第一次连接时肯定没有，所以直接创建
                    zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                long i = 0;
                while (!exit) {
                	long round = SystemUtils.getMemoryProportion();
                    if (round != i) {
                        i = round;
                        //更新Zookeeper节点数据
                        try {
                            String data = SystemUtils.getLocalHostAddress() + "/" + round;
                            zooKeeper.setData(path, data.getBytes(), -1);
                        } catch (KeeperException.SessionExpiredException e) {
                            e.printStackTrace();
                            //Session过期，重新连接
                            zooKeeper = getZooKeeperConnection();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        Thread.sleep(timeInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    /**
     * 获取Zookeeper连接
     * @return
     */
    private static ZooKeeper getZooKeeperConnection() {
        try {
            // Zookeeper连接闭锁cdl
            final CountDownLatch cdl = new CountDownLatch(1);
            final ZooKeeper zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        // 当连接成功时放开cdl
                        cdl.countDown();
                    }
                }
            });
            // cdl阻塞
            cdl.await();
            return zooKeeper;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 停止内存监控
     */
    public static void stopListen() {
        exit = true;
    }

}
