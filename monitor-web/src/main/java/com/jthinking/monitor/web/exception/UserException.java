package com.jthinking.monitor.web.exception;

/**
 * 自定义异常
 * @author JiaBochao
 * @version 2017-11-17 10:25:01
 */
public class UserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserException(String message) {
		super(message);
	}

}
