package com.jthinking.monitor.service;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Zookeeper监听器，监听数据实时更新
 * @author JiaBochao
 * @version 2017-11-17 10:04:38
 */
public class ServerMonitor {
    private static boolean exit = false;
    private static String connectString = "192.168.1.111:2181:2181";
    private static int sessionTimeout = 5000;
    private static final String BASE_PATH = "/linux-server";
    private static final WebSocketManager webSocketManager = new WebSocketManager("ws://localhost:8087/websocket/echo?project=monitor&module=memory");
    private static ZooKeeper zooKeeper;


    public static void exit() {
        exit = true;
    }

    public static boolean isConnected() {
        return zooKeeper == null ? false : true;
    }


    public static void startListen() {
        zooKeeper = getZooKeeperConnection();

        // 启动新的线程维持WebSocket存活
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!exit) {
                    webSocketManager.sendMessage("HaHaHa, I'm still alive~");
                    try {
                        Thread.sleep(3 * 60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        // 启动新的线程获取数据，并且设置永久监听
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 设置循环的目的是当basePath下有zNode节点被新增或删除时更新Map
                while (!exit) {
                    final CountDownLatch countDownLatch = new CountDownLatch(1);
                    List<String> children = null;
                    try {
                        children = zooKeeper.getChildren(BASE_PATH, new Watcher() {
                            @Override
                            public void process(WatchedEvent event) {
                                if (event.getType() == Event.EventType.NodeChildrenChanged) {
                                    // basePath的永久监听是通过当事件触发时放开当前循环的阻塞，再次循环添加监听实现的
                                    countDownLatch.countDown();
                                }
                            }
                        });
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for (String child : children) {
                        try {
                            byte[] data = zooKeeper.getData(BASE_PATH + "/" + child, new Watcher() {
                                @Override
                                public void process(WatchedEvent event) {
                                    if (event.getType() == Event.EventType.NodeDataChanged) {
                                        try {
                                            //将当前监听器this传给getData实现永久监听
                                            byte[] data = zooKeeper.getData(event.getPath(), this, null);
                                            String[] split = event.getPath().split("/");
                                            try {
                                                //~更新数据
                                                webSocketManager.sendMessage("u/" + split[split.length - 1] + "/" + new String(data));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } catch (KeeperException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    if (event.getType() == Event.EventType.NodeDeleted) {
                                        String[] split = event.getPath().split("/");
                                        //~删除数据
                                        webSocketManager.sendMessage("d/" + split[split.length - 1]);
                                    }

                                }
                            }, null);
                            try {
                                //~更新数据
                                webSocketManager.sendMessage("u/" + child + "/" + new String(data));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (KeeperException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        countDownLatch.await();
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
    public static ZooKeeper getZooKeeperConnection() {
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
}
