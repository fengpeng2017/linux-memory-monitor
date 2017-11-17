package com.jthinking.monitor;

import org.apache.log4j.Logger;

/**
 * 内存监控哨兵
 * @author JiaBochao
 * @version 2017-11-17 10:00:42
 */
public class MemorySentinel {

    public static void main(String[] args) {
        //开启内存监听
        MemoryListener.startListen();
    }


}
