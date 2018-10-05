package com.chen.utils;

/**
 * 	请求字典
 * @author chen
 *
 */
public class RequestCommand {
	//请求登陆
	public static final short LOGIN = 1;
	//请求注册
	public static final short REG = LOGIN + 1;
	//请求广播文本信息
	public static final short SEND_TEXT = REG + 1;
	//请求向指定用户发送文本信息
	public static final short SEND_TEXT_TO_ONE = SEND_TEXT + 1;
	//请求广播文件
	public static final short SEND_FILE = SEND_TEXT_TO_ONE + 1;
	//请求向指定用户发送文件
	public static final short SEND_FILE_TO_ONE = SEND_FILE + 1;
	//请求传送文件回执
	public static final short SEND_FILE_TO_ONE_1 = SEND_FILE_TO_ONE + 1;
	//请求开始传送文件
	public static final short SEND_FILE_TO_ONE_2 = SEND_FILE_TO_ONE_1 + 1;
}
