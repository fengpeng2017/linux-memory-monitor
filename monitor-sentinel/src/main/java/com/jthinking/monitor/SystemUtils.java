package com.jthinking.monitor;

import com.sun.management.OperatingSystemMXBean;
import org.apache.log4j.Logger;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 工具包
 * @author JiaBochao
 * @version 2017-11-17 10:01:07
 */
public class SystemUtils {
	private static Logger errorLogger = Logger.getLogger("errorLogger");
	
	private static OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

	/**
	 * 计算内存占用率
	 * @return
	 */
	public static long getMemoryProportion() {
		long totalPhysicalMemorySize = operatingSystemMXBean.getTotalPhysicalMemorySize();
        long freePhysicalMemorySize = operatingSystemMXBean.getFreePhysicalMemorySize();
        long usedPhysicalMemorySize = totalPhysicalMemorySize - freePhysicalMemorySize;
        long round = Math.round(usedPhysicalMemorySize * 1.0 / totalPhysicalMemorySize * 100);
        return round;
	}

	/**
	 * 获取本机名
	 * @return
	 */
	public static String getLocalHostName() {
		String name = null;
		try {
			name = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			errorLogger.error("", e);
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		return name;
	}

	/**
	 * 获取本机IP
	 * @return
	 */
	public static String getLocalHostAddress() {
		String address = null;
		try {
			address = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			errorLogger.error("", e);
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		return address;
	}
}
